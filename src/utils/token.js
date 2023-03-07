const key = 'pc-key'

const getToken = () => {
 return localStorage.getItem(key)
}
const setToken = (token) => {
 return localStorage.setItem(key, token)
}
const removeToken = () => {
 return localStorage.removeItem(key)
}

export { getToken, setToken, removeToken }