INSERT INTO usuarios (id, login, senha, role)
VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQsfSGA',
    'ADMIN'
) ON CONFLICT DO NOTHING;
