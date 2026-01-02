# Previsão de Chuva e Temperatura (Backend)

## Descrição
Este projeto é um backend em **Java** que fornece dados meteorológicos para um **frontend de previsão de chuva e temperatura**.  
O sistema processa arquivos CSV históricos fornecidos pelo **Instituto Nacional de Meteorologia (INMET)**, referentes aos anos de 2000 a 2025, para gerar médias e previsões simples de chuva e temperatura.

O objetivo é fornecer endpoints que entreguem dados processados para o frontend, permitindo a exibição de gráficos e estimativas de probabilidade de chuva.

---

## Tecnologias Utilizadas
- **Java 20**  
- **Spring Boot** (para criação de API REST)
- **CSV** (entrada de dados)
- **Bibliotecas Java padrão** (para tratamento de dados)
- **Distribuição Binomial** (para cálculo de probabilidade de chuva em 5 dias)

---

## Funcionamento

1. **Leitura de Dados**
   - O projeto lê arquivos CSV organizados por ano e cidade, baixados do INMET.
   - Cada CSV contém informações de **data, hora, precipitação, temperatura e umidade relativa**.

2. **Processamento**
   - Cálculo das **médias anuais de temperatura e chuva** para cada cidade.
   - Para previsão simplificada:
     - Utiliza **distribuição binomial** para calcular a **probabilidade de chover em pelo menos um dia dentro de 5 dias**.

3. **API Endpoints**
   - Exemplo de endpoint:
     ```
     GET /api/cidade/classificacaomedia?nome={cidade}
     ```
     - Retorna os dados processados:
       - Data  
       - Média de chuva
       - Classificação da Chuva
      ```
     GET /api/cidade/probabilidade5dias?nome={cidade}&dia={dia inicial}&mes={mes}
     ```
     - Retorna os dados processados:
       - Probabilidade de chuva nos próximos 5 dias
  
      ```
     GET /api/cidade/mediatotaltempumid?nome=brasilia
       ```
     - Retorna os dados processados:
       - Data
       - Media Temperatura e Umidade(%)
       
---

