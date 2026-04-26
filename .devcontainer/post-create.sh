#!/bin/bash

echo "🚀 Instalando dependencias..."

echo "Otorgando permisos de ejecución a Maven Wrapper..."
chmod +x /workspace/backend/mvnw /workspace/backend/mvnw.cmd

echo "Iniciando servicio MongoDB..."
cd /workspace/.devcontainer && docker-compose up -d mongo
echo "Esperando a que MongoDB esté listo..."
sleep 10

echo "Instalando dependencias del backend..."
cd /workspace/backend && ./mvnw install -DskipTests

echo "Instalando dependencias del frontend..."
cd /workspace/frontend && npm install

echo "Rellenando base de datos con datos de prueba..."
chmod +x /workspace/seed-db.sh
cd /workspace && timeout 300 ./seed-db.sh || true

echo ""
echo "Herramientas de Java listas:"
java -version
mvn -version

echo "Herramientas de Node listas:"
node -v
npm -v

echo "✅ Entorno de desarrollo preparado!"
echo ""
echo "📝 Comandos disponibles:"
echo "  Backend:  cd backend && ./mvnw spring-boot:run"
echo "  Frontend: cd frontend && npm run dev"
