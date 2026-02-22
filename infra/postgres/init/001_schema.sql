CREATE TABLE IF NOT EXISTS side_effects (
  id BIGSERIAL PRIMARY KEY,
  message_id UUID NOT NULL,
  payload JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS processed_messages (
  message_id UUID PRIMARY KEY,
  processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_side_effects_message_id
  ON side_effects(message_id);
