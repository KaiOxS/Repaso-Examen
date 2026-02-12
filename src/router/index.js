import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Producto from '@/components/Producto.vue'
import Venta from '@/components/Venta.vue'

const routes = [
  {
    path: '/',
    redirect: '/producto'
  },
  {
    path: '/producto',
    name: 'producto',
    component: Producto
  },
  {
    path: '/venta',
    name: 'venta',
    component: Venta
  },

]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
