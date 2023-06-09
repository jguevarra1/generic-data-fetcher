const colors = require('tailwindcss/colors')

module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: colors.black,
      white: colors.white,
      gray: colors.gray,
      emerald: colors.emerald,
      indigo: colors.indigo,
      yellow: colors.yellow,
      red: colors.red,
      transparent: 'transparent',
      'figma_purple': "#373A70",
      'dark-purple': "#16103A",
      'white': "#FFFFFF",
    },
    extend: {
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}
