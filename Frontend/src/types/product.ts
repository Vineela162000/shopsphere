// TypeScript types = the "shape" of the JSON our backend sends.
// They match ProductResponse.java field by field.

export type Occasion = 'EVERYDAY' | 'FESTIVE' | 'BRIDAL'

export interface Product {
  id: number
  sku: string
  name: string
  description: string | null
  category: string
  weave: string
  occasion: Occasion
  color: string
  price: number
  stock: number
  inStock: boolean
  imageUrl: string | null
  bestseller: boolean
}

// Matches Spring's PagedModel: { content: [...], page: {...} }
// <T> is a generic, same idea as Page<ProductResponse> in Java.
export interface Page<T> {
  content: T[]
  page: {
    size: number
    number: number
    totalElements: number
    totalPages: number
  }
}