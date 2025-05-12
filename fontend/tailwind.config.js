/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
    "./src/app/**/*.{html,ts}"
  ],
  theme: {
    extend: {
      colors: {
        'acik-pembe': '#F4ABC4',
        'lavanta-grisi': '#595B83',
        'gece-yarisi': '#333456',
        'lacivert-derin': '#060930',
        'beyaz': '#FFFFFF', // Eğer tanımlı değilse ekleyin
      },
    },
  },
  plugins: [],
  important: true
}
