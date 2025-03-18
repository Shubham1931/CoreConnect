/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
      "./src/**/*.{html,js,ts,jsx,tsx}", // Adjust paths based on your project structure
      "./src/main/resources/templates/**/*.html", // For Spring Boot + Thymeleaf templates
    ],
    theme: {
      extend: {},
    },
    plugins: [],
    darkMode:'class',
  };
  