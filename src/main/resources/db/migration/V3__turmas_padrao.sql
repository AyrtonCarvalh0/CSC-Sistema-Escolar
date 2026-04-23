INSERT INTO turma (id, nome, idade_min, idade_max, capacidade, valor_mensalidade)
VALUES
  (gen_random_uuid(), 'Maternal Baby', 1, 2, 20, 240.00),
  (gen_random_uuid(), 'Maternal',      2, 3, 20, 240.00),
  (gen_random_uuid(), 'Jardim',        3, 4, 25, 240.00),
  (gen_random_uuid(), 'Pré',           5, 6, 25, 240.00),
  (gen_random_uuid(), 'Primeiro Ano',  6, 7, 30, 260.00)
ON CONFLICT DO NOTHING;
