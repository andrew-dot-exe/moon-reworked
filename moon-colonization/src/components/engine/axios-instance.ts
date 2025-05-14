import axios from 'axios'
import type { AxiosInstance } from 'axios'

// Get the base URL from environment variables or default to current origin
const baseURL = import.meta.env.VITE_API_URL || window.location.origin

// Create axios instance with configuration
const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  timeout: 30000, // 30 seconds
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
})

// Request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    // You can add authorization headers here if needed
    // const token = localStorage.getItem('token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config
  },
  (error) => Promise.reject(error),
)

// Response interceptor
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle global errors here
    return Promise.reject(error)
  },
)

export default axiosInstance
