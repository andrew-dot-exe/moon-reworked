import axiosInstance from '../engine/axios-instance'

const API_URL = '/api/user'
export const userApi = {
  /**
   * Get all users
   */
  login: async (email: string, password: string) => {
    const response = await axiosInstance.post(
      `${API_URL}/login`,
      {
        email: email,
        password: password,
      },
      {
        withCredentials: false,
      },
    )
    if (response.status != 200) {
      console.error(`Error at /api/login. The data is ${response.data}`)
    }
    return response.status
  },

  // logoff?

  register: async (email: string, password: string) => {
    const response = await axiosInstance.post(`${API_URL}/create`, {
      email: email,
      password: password,
    })
    if (response.status != 200) {
      console.error(`Error at /api/create. The data is ${response.data}`)
    }
    return response.headers
  },
}

export default userApi
