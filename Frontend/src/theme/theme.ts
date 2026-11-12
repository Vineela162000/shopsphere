import { createTheme } from '@mui/material/styles'

// One place for the shop's colors and fonts (like the Figma color variables).
export const theme = createTheme({
  palette: {
    primary: { main: '#7A1F3D' },                         // maroon
    secondary: { main: '#B8860B' },                       // antique gold
    background: { default: '#FBF7F0', paper: '#FFFFFF' }, // ivory page, white cards
  },
  typography: {
    fontFamily: 'system-ui, "Segoe UI", Roboto, sans-serif',
    h5: { fontFamily: 'Georgia, serif', fontWeight: 600 },
  },
  shape: { borderRadius: 10 },
})