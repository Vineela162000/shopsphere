import { Box, Button, Card, CardContent, CardMedia, Chip, Stack, Typography } from '@mui/material'
import type { Product } from '../types/product'

// Shown if a photo is missing or its link breaks
const FALLBACK_IMAGE =
  'data:image/svg+xml;utf8,' +
  encodeURIComponent(
    '<svg xmlns="http://www.w3.org/2000/svg" width="300" height="400">' +
      '<rect width="100%" height="100%" fill="#F3E9DC"/>' +
      '<text x="50%" y="50%" text-anchor="middle" fill="#7A1F3D" font-family="Georgia" font-size="22">ShopSphere</text>' +
      '</svg>',
  )

// Formats 18500 as ₹18,500
const rupees = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 })

type ProductCardProps = {
  product: Product
}

// A React component = a function that returns what to show on screen.
// "props" = the inputs it receives (here: one saree).
export default function ProductCard({ product }: ProductCardProps) {
  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Box sx={{ position: 'relative' }}>
        <CardMedia
          component="img"
          image={product.imageUrl ?? FALLBACK_IMAGE}
          alt={product.name}
          loading="lazy"
          onError={(event) => {
            ;(event.target as HTMLImageElement).src = FALLBACK_IMAGE
          }}
          sx={{ aspectRatio: '3 / 4', objectFit: 'cover', opacity: product.inStock ? 1 : 0.5 }}
        />
        <Stack direction="row" spacing={1} sx={{ position: 'absolute', top: 8, left: 8 }}>
          {product.bestseller && <Chip label="Bestseller" color="secondary" size="small" />}
          {!product.inStock && <Chip label="Out of stock" size="small" sx={{ bgcolor: 'white' }} />}
        </Stack>
      </Box>

      <CardContent sx={{ flexGrow: 1 }}>
        <Typography variant="caption" color="text.secondary">
          {product.weave}
        </Typography>
        <Typography variant="subtitle1" sx={{ lineHeight: 1.3, mb: 1 }}>
          {product.name}
        </Typography>
        <Typography variant="h6" color="primary">
          {rupees.format(product.price)}
        </Typography>
      </CardContent>

      <Box sx={{ p: 2, pt: 0 }}>
        <Button fullWidth variant="contained" disabled={!product.inStock}>
          {product.inStock ? 'Add to Bag' : 'Sold out'}
        </Button>
      </Box>
    </Card>
  )
}