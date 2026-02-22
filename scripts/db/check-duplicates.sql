SELECT message_id, COUNT(*) AS qty
FROM side_effects
GROUP BY message_id
HAVING COUNT(*) > 1
ORDER BY qty DESC;
