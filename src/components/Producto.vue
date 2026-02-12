<template>
    <div class="container">
        <h3>Productos</h3>
        <div class="formulario">
            <form action="#">
                <h2>Ingresar un producto: </h2>
                <label for="id_codigoBarras">Código de Barras: </label>
                <input v-model.number="codigoBarras" type="number" id="id_codigoBarras">
                <label for="id_nombre">Nombre: </label>
                <input v-model="nombre" type="text" id="id_nombre">
                <label for="id_categoria">Categoría: </label>
                <input v-model="categoria" type="text" id="id_categoria">
                <label for="id_stock">Stock: </label>
                <input v-model.number="stock" type="number" id="id_stock">
                <label for="id_precio">Precio: </label>
                <input v-model.number="precio" type="number" id="id_precio">
            </form>
        </div>
        <button @click="crearProducto({ codigoBarras, nombre, categoria, stock, precio })">Guardar</button>
        <div class="mensaje" v-if="mensaje">
            <p>{{ mensaje }}</p>
        </div>
    </div>
</template>

<script>
import { crearFachada } from '@/clients/ProductoClient';
export default {
    data() {
        return {
            codigoBarras: 0,
            nombre: "",
            categoria: "",
            stock: 0,
            precio: 0,
            mensaje: "",
        }
    },
    methods: {
        async crearProducto(body) {
            await crearFachada(body);
            this.mensaje = "Producto creado exitosamente"
        }
    }
}
</script>

<style scoped>
.container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    text-align: center;
}

h3 {
    color: #333;
    margin-bottom: 30px;
    font-size: 28px;
}

.formulario {
    background-color: #f9f9f9;
    padding: 25px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

form h2 {
    color: #555;
    margin-bottom: 20px;
    font-size: 20px;
}

label {
    display: block;
    margin-top: 15px;
    margin-bottom: 5px;
    color: #666;
    font-weight: 500;
}

input {
    width: 100%;
    padding: 10px;
    margin-bottom: 15px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
    box-sizing: border-box;
}

input:focus {
    outline: none;
    border-color: #4CAF50;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 12px 30px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    font-weight: 600;
    transition: background-color 0.3s;
}

button:hover {
    background-color: #45a049;
}

.mensaje {
    margin-top: 20px;
    padding: 15px;
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
    border-radius: 4px;
}
</style>