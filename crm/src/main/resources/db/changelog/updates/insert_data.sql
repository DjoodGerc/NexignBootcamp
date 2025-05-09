-- Заполнение таблицы role
INSERT INTO roles (name) VALUES
('ROLE_MANAGER'),
('ROLE_SUBSCRIBER');
-- Заполнение таблицы subscriber
INSERT INTO subscriber (password, login, passport_data) VALUES
('$2y$10$fMopG3eXQMpCAmixYiYeQuPXOoQ/.yjdE8NH/eZB0MDjEOcfrKMTm', 'admin', NULL),
('$2y$10$VrpXku0HNDMHH8yIky7q1.SCUmFBnYzXAxgEcT.ZFSQrJJgEC3gdW', '79123456789', '1234567890'),
('$2y$10$VrpXku0HNDMHH8yIky7q1.SCUmFBnYzXAxgEcT.ZFSQrJJgEC3gdW', '79456789012', '2345678901'),
('$2y$10$VrpXku0HNDMHH8yIky7q1.SCUmFBnYzXAxgEcT.ZFSQrJJgEC3gdW', '79567890123', '3456789012'),
('$2y$10$VrpXku0HNDMHH8yIky7q1.SCUmFBnYzXAxgEcT.ZFSQrJJgEC3gdW', '79890123456', '4567890123'),
('$2y$10$VrpXku0HNDMHH8yIky7q1.SCUmFBnYzXAxgEcT.ZFSQrJJgEC3gdW', '79012345678', '5678901234');

-- Заполнение таблицы subscriber_role
INSERT INTO subscriber_roles (roles_id, subscriber_id) VALUES
(1, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6);