import type { Page, Product } from '../types/product'

// Consuming our REST API: GET /api/products?page=0&size=12
export async function fetchProducts(page = 0, size = 12): Promise<Page<Product>> {
  const response = await fetch(`/api/products?page=${page}&size=${size}`)
  if (!response.ok) {
    throw new Error(`Could not load sarees (HTTP ${response.status})`)
  }
  return response.json()
}