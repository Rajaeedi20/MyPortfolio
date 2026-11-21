package com.rajarao.portfolio.config;

import com.rajarao.portfolio.model.*;
import com.rajarao.portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/** Seeds the portfolio content on first start. Edit this file to update your portfolio. */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ExperienceRepository experiences;
    private final ProjectRepository projects;
    private final SkillGroupRepository skills;

    @Override
    public void run(String... args) {
        if (experiences.count() == 0) seedExperience();
        if (projects.count() == 0) seedProjects();
        if (skills.count() == 0) seedSkills();
        else updateDevOpsSkills();
    }

    private void seedExperience() {
        experiences.saveAll(List.of(
            Experience.builder().sortOrder(1).current(true)
                .company("HCLTech").role("Senior Software Developer")
                .location("India").period("Oct 14, 2026 – Present")
                .domain("Enterprise Full Stack Development")
                .tech("Java,Spring Boot,Microservices,React.js,REST APIs")
                .highlights(List.of(
                    "Designing and building enterprise-grade backend services with Java and Spring Boot.",
                    "Developing responsive React.js front-ends and integrating them with REST APIs.",
                    "Contributing to code reviews, CI/CD pipelines and Agile delivery with cross-functional teams."))
                .build(),

            Experience.builder().sortOrder(2).current(false)
                .company("CelesteZoi Pvt. Ltd").role("Full Stack Developer")
                .location("Hyderabad").period("Jun 2024 – Jul 2026")
                .domain("Cosmetic & Service Booking Platform")
                .tech("Java,Spring Boot,Spring Data JPA,Hibernate,React.js,Redux Toolkit,MySQL,MongoDB,GitHub Actions,AWS")
                .highlights(List.of(
                    "Designed, coded and unit tested backend services for a booking platform covering service booking, trip management, café management, membership, billing and user administration.",
                    "Developed and maintained 20+ RESTful APIs with Spring Boot, Spring Data JPA and Hibernate, including authentication, authorization and validation.",
                    "Built scalable microservice-style services for booking workflows, payment processing and customer data.",
                    "Contributed to GitHub Actions CI/CD pipelines deploying to AWS, reducing release cycle time by 40%.",
                    "Optimized MySQL and MongoDB schemas, improving query performance by 35%.",
                    "Improved front-end responsiveness with React code splitting, lazy loading and Redux Toolkit.",
                    "Conducted structured code reviews, reducing the bug rate by 15%."))
                .build(),

            Experience.builder().sortOrder(3).current(false)
                .company("Sand Blaze Digital Pvt. Ltd").role("Junior Software Developer")
                .location("Hyderabad").period("Mar 2024 – May 2024")
                .domain("ERP Application (SCHOOLSERP)")
                .tech("Java,Spring Boot,REST APIs,SQL")
                .highlights(List.of(
                    "Created admin panels and dashboards and implemented RESTful APIs using Java and Spring Boot.",
                    "Designed the database schema and implemented CRUD operations to store student data.",
                    "Handled API integration, code reviews, unit testing and application deployments."))
                .build(),

            Experience.builder().sortOrder(4).current(false)
                .company("Protocol India Pvt. Ltd").role("Trainee Software Engineer")
                .location("Hyderabad").period("Apr 2023 – Oct 2023")
                .domain("ERP Web Application (MYERP)")
                .tech("React.js,Node.js,Express.js,Bootstrap,MySQL")
                .highlights(List.of(
                    "Developed component-level solutions for an ERP web app spanning 5+ modules: employees, production, inventory and reporting.",
                    "Built responsive React.js interfaces with reusable components and Bootstrap, cutting UI development time by 30%.",
                    "Extended Node.js/Express APIs with CRUD operations and role-based access control for 50+ users.",
                    "Reduced data-entry errors by 40% through better form validation and real-time checks.",
                    "Worked with MySQL supporting 10,000+ records with optimized queries."))
                .build()
        ));
    }

    private void seedProjects() {
        projects.saveAll(List.of(
            Project.builder().sortOrder(1)
                .title("Cosmetic & Service Booking Platform").company("CelesteZoi")
                .description("Full-featured booking platform with service booking, trip and café management, memberships, billing and user administration, backed by 20+ REST APIs and integrated payment workflows.")
                .tech("Java,Spring Boot,JPA,Hibernate,React.js,Redux Toolkit,MySQL,MongoDB,AWS")
                .impact("Serves 1,000+ users · 40% faster releases").build(),
            Project.builder().sortOrder(2)
                .title("MYERP – Multi-module ERP").company("Protocol India")
                .description("Multi-module ERP web application covering HR, production, inventory and business reporting, built from scratch with reusable React components and role-based access control.")
                .tech("React.js,Node.js,Express.js,Bootstrap,MySQL")
                .impact("Built in 6 months · 50% operational efficiency gain").build(),
            Project.builder().sortOrder(3)
                .title("SCHOOLSERP").company("Sand Blaze Digital")
                .description("School ERP with admin panels, dashboards and student data management on a Spring Boot REST backend.")
                .tech("Java,Spring Boot,REST APIs,SQL")
                .impact("Admin dashboards · student CRUD").build()
        ));
    }

    private void seedSkills() {
        skills.saveAll(List.of(
            SkillGroup.builder().sortOrder(1).category("Languages").items("Java 8,JavaScript").build(),
            SkillGroup.builder().sortOrder(2).category("Backend").items("Spring Boot,Spring MVC,Spring Data JPA,Hibernate,REST APIs,Microservices,Kafka").build(),
            SkillGroup.builder().sortOrder(3).category("Front End").items("React.js,Redux Toolkit,React Hooks,Context API,HTML5,CSS3,Bootstrap").build(),
            SkillGroup.builder().sortOrder(4).category("Database").items("MySQL,MongoDB,Schema Design,Query Optimization").build(),
            SkillGroup.builder().sortOrder(5).category("Security").items("JWT Authentication,Role-Based Access Control").build(),
            SkillGroup.builder().sortOrder(6).category("DevOps").items("Docker,GitHub Actions,AWS,Git,CI/CD").build(),
            SkillGroup.builder().sortOrder(7).category("Practices").items("Agile/Scrum,JUnit,Swagger,Postman,Code Reviews").build()
        ));
    }

    private void updateDevOpsSkills() {
        skills.findAllByOrderBySortOrderAsc().stream()
            .filter(group -> "DevOps".equalsIgnoreCase(group.getCategory()))
            .findFirst()
            .ifPresent(group -> {
                String updatedItems = Arrays.stream(group.getItems().split(","))
                    .map(String::trim)
                    .filter(item -> !item.equalsIgnoreCase("Jenkins"))
                    .collect(Collectors.joining(","));
                if (!updatedItems.equals(group.getItems())) {
                    group.setItems(updatedItems);
                    skills.save(group);
                }
            });
    }
}
