# 🏢 Central_FESP-PR — Central de Relacionamento FESP

> **Unificação de Informações e Relacionamento FESP**  
> Sistema web interno de gestão institucional para centralização de atendimentos, eventos, solicitações acadêmicas e denúncias da FESP PR.

---

## 📌 Sobre o Projeto

O **Central_FESP** é uma solução desenvolvida para centralizar, automatizar e organizar os processos da Central de Relacionamento da FESP PR. O sistema substitui fluxos manuais em papel e planilhas por uma plataforma digital segura, integrada e dividida entre o painel administrativo e o portal de autoatendimento do aluno.

### 🎯 Principais Objetivos
- **Centralização:** Eliminar controles informais via planilhas e mensagens pessoais de WhatsApp.
- **Agilidade:** Automatizar aprovações de eventos e solicitações acadêmicas com suporte a fluxos multi-etapas (Central + Diretoria).
- **Transparência e Sigilo:** Canal formal para Reclamações e Denúncias sigilosas/anônimas com protocolo.
- **Autoatendimento:** Digitalização de processos como Trancamento, Cancelamento e Transferência.

---

## 🚀 Módulos do Sistema

### 👨‍💼 Visão Administrativa (Central / Gestão)
* **Visão Geral & Dashboard:** Métricas gerais de atendimento e navegação rápida.
* **Fila de Aprovações:** Gestão de solicitações e eventos pendentes com verificação de conflito de agenda.
* **Agenda & Gestão de Salas:** Controle de eventos internos/externos e alocação de espaços físicas/salas.
* **Reclamações e Denúncias:** Gestão centralizada de manifestações com canal exclusivo e sigiloso para denúncias.
* **Trancamento / Cancelamento / Transferência:** Módulo dedicado à digitalização do formulário acadêmico.

### 🎓 Portal do Aluno
* **Início:** Ações rápidas e acompanhamento de solicitações recentes.
* **Eventos:** Consulta à agenda pública e formulário para solicitação de espaços.
* **Minhas Solicitações:** Histórico completo com status em tempo real.
* **Canal de Manifestações:** Formulário único para envio de sugestões, reclamações e denúncias anônimas.
* **Solicitações Acadêmicas:** Autoatendimento para Trancamento, Cancelamento e Transferência.

---

## 🎨 Identidade Visual & Cores

O projeto utiliza a paleta corporativa oficial extraída da marca FESP PR.:

| Elemento | Cor | Hexadecimal |
| :--- | :--- | :--- |
| **Navy (Cor Primária)** | Azul Escuro | `#1A1D56`. |
| **Light Blue (Destaque)** | Azul Claro | `#3BACE2`. |

---

## 🛠️ Stack Tecnológica

O desenvolvimento do projeto segue a seguinte trilha tecnológica incremental.:

* **Frontend:** HTML5, CSS3, JavaScript (ES6+), React.js.
  * *UI/Gráficos:* Recharts (Dashboard).
  * *Calendário:* react-big-calendar.
* **Backend:** Java + Spring Boot (Controllers, Services, Repositories, Spring Data JPA).
* **Banco de Dados:** PostgreSQL.
* **Prototipagem & UI/UX:** Figma.

---

## ⚙️ Arquitetura do Sistema

O UIRF é estruturado como um **sistema web interno single-tenant**.
* Frontend desacoplado em React.
* API RESTful em Java/Spring Boot.
* Banco de dados relacional para persistência dos fluxos.
* Controle de acesso baseado em perfis (Alunos, Central de Relacionamento, Secretaria, Docentes e Diretoria).

---

## 📋 Status de Desenvolvimento

- [x] Levantamento de Requisitos (RF e RNF) e Mapeamento BPMN (AS-IS).
- [x] Protótipo HTML/CSS Navegável com alternador de visões (Admin / Aluno).
- [x] Definição da Identidade Visual e conceitos do Logotipo.
- [ ] Implementação de componentes em React (StatCards, Recharts, Calendário).
- [ ] Construção dos endpoints REST no Spring Boot.
- [ ] Modelagem e integração do Banco de Dados PostgreSQL.

---

## 👥 Equipe do Projeto

CRISTOPHER GUILHERME DE ALMEIDA
MATHEUS FERREIRA DA SILVA
RENAN CARLOS VIEIRA SILVA
TALITA PEREIRA ROCHA
THIAGO PAIVA VON MÜLLER BERNECK



Projeto desenvolvido para a faculdade **FESP PR**.