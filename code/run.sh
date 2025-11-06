#!/bin/bash

echo "╔════════════════════════════════════════════════════════════╗"
echo "║       Design Patterns Demo - Startup Script               ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Navigate to project directory
cd "$(dirname "$0")"

echo "📦 Cleaning and installing dependencies..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    echo "🚀 Starting application..."
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    
    mvn spring-boot:run
else
    echo ""
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi
