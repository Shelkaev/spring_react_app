INSERT INTO usr VALUES (1, 'test', 'Тест', '$2a$10$v9fYrqTp6x6yn440x5uFbeacrLwaU6A3WpbE3LYlVEvbloS7ihPmq', 'Тестович', 'USER', 'Тестов');
INSERT INTO usr VALUES (2, 'admin', 'Админ', '$2a$10$v9fYrqTp6x6yn440x5uFbeacrLwaU6A3WpbE3LYlVEvbloS7ihPmq', 'Админович', 'ADMIN', 'Админов');
INSERT INTO usr VALUES (3, 'test_1', 'Тест_1', '$2a$10$v9fYrqTp6x6yn440x5uFbeacrLwaU6A3WpbE3LYlVEvbloS7ihPmq', 'Тестович', 'USER', 'Тестов');
INSERT INTO debit_card VALUES (1, 1000000, '1111211111111111', 'DEBIT', '2025-05-05 12:51:13.919', 1);
INSERT INTO debit_card VALUES (2, 1000000, '1111229199296722', 'DEBIT', '2025-05-05 12:51:13.919', 1);
INSERT INTO debit_card VALUES (3, 1000000, '1111211111111112', 'DEBIT', '2025-05-05 12:51:13.919', 1);
INSERT INTO credit_card VALUES ( 1, 1000000, 1111011111111112, 'CREDIT', '2025-05-05 12:51:13.919', 0.2, 0.1, 1000000, null, 0.01, 'ACTIVE',0,1);
INSERT INTO transfer VALUES (1, 60000, 0, '2022-05-12 23:14:38.627', '1111229199296722', 'BLOCK', 'INTERNAL_TO_DEBIT_CARD', NULL , 1);