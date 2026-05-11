#!/bin/bash
echo "Parando e removendo o banco de dados..."
docker compose down -v

echo "Subindo banco limpo..."
docker compose up -d

echo "Aguardando PostgreSQL inicializar..."
sleep 5

echo "Banco pronto! Inicie o Spring Boot."
echo "O Flyway vai recriar tudo automaticamente."
