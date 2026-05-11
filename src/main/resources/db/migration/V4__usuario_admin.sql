INSERT INTO usuarios (id, login, senha, role)
VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$10$slMoX6eEAMaBjOBaK8ov7.U1sSfDPCnFAMVYPoSHwPqMb3VujiFuK',
    'ADMIN'
) ON CONFLICT DO NOTHING;
