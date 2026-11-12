import { useEffect, useState } from 'react'
import { Alert, AppBar, Box, Button, CircularProgress, Container, Grid, Toolbar, Typography } from '@mui/material'
import ProductCard from './components/ProductCard'
import { fetchProducts } from './api/productsApi'
import type { Product } from './types/product'

export default function App() {
  // React state: values that, when changed, make the screen update by itself
  const [products, setProducts] = useState<Product[]>([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [total, setTotal] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // useEffect: "run this when the page opens, and again whenever `page` changes"
  useEffect(() => {
    let ignore = false
    fetchProducts(page)
      .then((data) => {
        if (ignore) return
        // page 0 → replace the list; next pages → add to the end
        setProducts((old) => (page === 0 ? data.content : [...old, ...data.content]))
        setTotalPages(data.page.totalPages)
        setTotal(data.page.totalElements)
      })
      .catch((e: Error) => {
        if (!ignore) setError(e.message)
      })
      .finally(() => {
        if (!ignore) setLoading(false)
      })
    return () => {
      ignore = true // a newer request replaced this one, so drop its result
    }
  }, [page])

  const loadMore = () => {
    setLoading(true)
    setPage((p) => p + 1)
  }

  return (
    <>
      <AppBar position="sticky" elevation={0}>
        <Toolbar>
          <Typography variant="h5" component="h1">
            ShopSphere
          </Typography>
          <Typography sx={{ ml: 2, opacity: 0.8 }}>Handwoven sarees</Typography>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h5">All sarees</Typography>
        <Typography color="text.secondary" sx={{ mb: 3 }}>
          {total} sarees
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}. Is the backend running on port 8080?
          </Alert>
        )}

        {/* Grid: 1 card per row on phones, 2 on tablets, 3 on laptops, 4 on big screens */}
        <Grid container spacing={3}>
          {products.map((product) => (
            <Grid key={product.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
              <ProductCard product={product} />
            </Grid>
          ))}
        </Grid>

        <Box sx={{ textAlign: 'center', mt: 4 }}>
          {loading ? (
            <CircularProgress />
          ) : (
            page + 1 < totalPages && (
              <Button variant="outlined" size="large" onClick={loadMore}>
                Load more sarees
              </Button>
            )
          )}
        </Box>
      </Container>
    </>
  )
}
