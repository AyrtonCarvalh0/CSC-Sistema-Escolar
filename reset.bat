@echo off
echo Parando e removendo o banco de dados...
docker compose down -v

echo Subindo banco limpo...
docker compose up -d

echo Aguardando PostgreSQL inicializar...
timeout /t 5

echo Banco pronto! Agora inicie o Spring Boot no IntelliJ.
echo O Flyway vai recriar todas as tabelas e dados padrão automaticamente.
pause
