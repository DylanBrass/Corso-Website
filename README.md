# Corso-Website
Welcome to Corso Electrique Website, an innovative website crafted by a dynamic team of four ambitious students in their final year of college. Corso's website is designed to meet the specific needs of a startup electrician company such as promoting electrical construction and repair services, monitoring the current projects' development, and providing statistics about the company's performance. 

## Project Description
This website seamlessly connects customers and administrators, offering unique experiences tailored to their needs.

### Customized User Experience:
Admin users enjoy a plethora of privileges, enabling them to modify the front page layout dynamically which includes updating the images of Corso's previous work, modifying the services they offer, selecting FAQs to be displayed, and curating customer testimonies. Admin users have the capability to monitor the development of current projects, update the progress of ongoing orders, and efficiently manage incoming service requests. This ensures a streamlined workflow for the electrician company.
Customers can seamlessly request services, leave reviews, and modify their profile information to ensure a personalized experience.

### Effortless Navigation:
Even without logging in, anonymous users can explore the main page, gaining insights into the services offered, viewing project pictures, and reading selected testimonies and FAQs.

### Performance Analytics:
The website provides valuable statistics about the company's performance, empowering decision-making and strategic planning.

## Table of Contents
- [Getting Started](#getting-started)
  - [Setting Up Your Development Environment](#setup-environment)
  - [Usage Instructions](#usage-instructions)
- [Credits](#credits)

<a name="getting-started"></a>
## Getting Started
### Build with

#### Frontend Development
React.js: Leveraging the power of React to build a seamless and interactive customer portal and admin dashboard.

#### Backend Development
Spring Boot (Java): A robust and efficient framework for building scalable and maintainable backend services.
Docker: Containerizing the backend for simplified deployment and scalability.

#### Database
Digital Ocean MySQL Database Cluster: A reliable and scalable database solution for storing and retrieving data related to orders, customers, reviews, FAQs, galleries, and services.

#### Authentication
Auth0: Auth0 is an easy-to-implement, adaptable authentication and authorization platform providing secure and seamless user authentication for both customers and admins. 

#### Image Storage
Cloudinary: Efficiently storing and managing images that enhance the visual appeal of the website.

#### Email Services:
Gmail SMTP System: Leveraging Gmail's SMTP system for reliable email services, including order confirmations and review notifications.

#### Performance Analytics
Recharts: Integrating dynamic and interactive charts and graphs to present valuable statistics about the company's performance.

<a name="setup-environment"></a>
### Setting Up Your Development Environment
Ensure you have Docker installed on your machine. If not, download and install Docker from <a href="https://www.docker.com/">Docker</a>
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/DylanBrass/Corso-Website.git
    ```
2. **Run backend through Docker:**
   ```bash
   cd corso_website/springboot/
   docker-compose up --build
   ```
This command initializes Docker and builds the backend Spring Boot application. Make sure to wait until the process is complete.

Make sure to include a .env file containing this (for the frontend):
  ```bash
  REACT_APP_BE_HOST=http://localhost:8080/
  ```
3. **Run frontend:**
  ```bash
  cd .\corso_website_fe\
  npm install
  npm start
  ```
This installs the necessary libraries for the frontend and starts the development server.
Open your web browser and go to http://localhost:3000 to access the Corso Electrique Website.

<a name="usage-instructions"></a>
### Usage Instructions
#### Admin's Perspective
- Use the provided admin account (which has the "Admin" role) from auth0
- You will land on the Admin Dashboard giving you access to a variety of features such as tracking orders, analyzing the company's performance, as well as modifying the projects' pictures, services, FAQs, and testimonies displayed on the main page.
Here are some guidelines on how to use three of the admin features, the others are left for you to explore!
##### Track specific order's progression 
1. To track the progress of a current order, click on the "Current Orders" tile, the middle tile on the first row:
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/eeb713fe-c8ba-4d47-a366-c128f9b9025b)
2. Click on the specific order you want to track, you will be able to see the order's progression and modify it.
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/8095732f-46a8-4a50-a47a-a9a17836725e)
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/fbbaf83a-5587-478a-a613-e94a41422e69)
##### Analyze the company's performance
1. Click on the Reports Tile from the Admin Dashboard
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/bd782427-c205-4013-b9ee-1993a620d0d5)
2. Select the desired report and type of graph
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/c75eb449-41ee-44e5-8212-ffb03f73e4c3)
##### Modify the main page carousel
1. Edit the carousel of previous projects, displayed on the main page, by adding, removing, or modifying the galleries.
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/aa25b937-1860-456d-bb14-d94d24a3d23d)

#### Customer's Perspective
- Create your customer account
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/26f92aad-bbbd-4b42-97ce-0e755264a2cf)
- You will be greeted with this main page
![Screenshot 2024-02-18 140911](https://github.com/DylanBrass/Corso-Website/assets/46633364/7b81e237-d047-4901-9742-b2ce65b25e9f)
- By clicking on your profile on the top right, you will land on your profile page from where you can view your profile information, your orders, your reviews, and you can request an order.
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/c6e9632e-c96f-44ba-b9bc-4c5afaba1b85)
Here are some guidelines on how to use two of the customer features, the others are left for you to explore!
##### Create an order request
1. Create an order request by clicking the blue button on your profile page
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/1adcaf18-fab9-4ed3-b444-b9bfdf5dd350)
##### Write a review
1. Click on the "View Your Reviews" tile on your profile page
2. If no reviews are present, this message will appear:
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/dbb44b4e-e6de-4284-a0d9-08314a77e297)
3. Click on the "Create Review" button to be able to leave a review by filling out this form:
![image](https://github.com/DylanBrass/Corso-Website/assets/46633364/1de35daa-d559-4975-879c-42605e1b8be6)

<a name="credits"></a>
### Credits
Meet the brilliant minds behind the magic of Corso Electrique, where professionalism meets a dash of enchantment:
- **Dylan Brassard ([@DylanBrassard](https://github.com/DylanBrass)):** Dylan worked on database architecture, backend development, and frontend design. He also helped implementing a secure authentication system using Auth0.
- **Denisa Hategan ([@DenisaHategan](https://github.com/DenisaNicoletaH)):** Denisa's proficiency in both backend and frontend development added a valuable dimension to the project. Her collaborative spirit and problem-solving abilities greatly enhanced the overall quality of Corso's website.
- **Karina Evangelista ([@KarinaEvangelista](https://github.com/KarinaSofia)):** Karina brought her skills to both the backend and frontend development, contributing to the seamless functioning of the website. Her attention to detail and dedication played a key role in shaping the user experience.
- **Mila Kehayova ([@MilaKehayova](https://github.com/Mila5847)):** Mila made invaluable contributions to both backend and frontend development, playing a key role in crafting the website's features and functionality. Her dedication to delivering quality ensured a platform that is both robust and user-friendly.

