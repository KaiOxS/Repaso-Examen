<template>
    <div class="venta-container">
        <h1>Nueva Venta</h1>

        <div class="form-section">
            <div class="form-group">
                <label>Número Venta:</label>
                <input v-model="numeroVenta" type="text" placeholder="Ingrese número de venta">
            </div>

            <div class="form-group">
                <label>Cédula Cliente:</label>
                <input v-model="cedula" type="text" placeholder="Ingrese cédula">
            </div>
        </div>

        <div class="productos-section">
            <h2>Agregar Productos</h2>
            <div class="agregar-producto">
                <div class="form-group">
                    <label>Código Barras:</label>
                    <input v-model="codigoBarras" type="text" placeholder="Código barras">
                </div>

                <div class="form-group">
                    <label>Cantidad:</label>
                    <input v-model.number="cantidad" type="number" placeholder="Cantidad" min="1">
                </div>

                <button @click="agregarProducto">Agregar</button>
            </div>

            <div v-if="mensaje" :class="['mensaje', mensaje.tipo]">
                {{ mensaje.texto }}
            </div>
        </div>

        <div class="lista-productos">
            <h2>Productos Agregados</h2>
            <table v-if="productos.length > 0">
                <thead>
                    <tr>
                        <th>Código Barras</th>
                        <th>Descripción</th>
                        <th>Cantidad</th>
                        <th>Precio Unitario</th>
                        <th>Subtotal</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(producto, index) in productos" :key="index">
                        <td>{{ producto.codigoBarras }}</td>
                        <td>{{ producto.descripcion }}</td>
                        <td>{{ producto.cantidad }}</td>
                        <td>${{ producto.precioUnitario }}</td>
                        <td>${{ producto.subtotal }}</td>
                        <td><button @click="eliminarProducto(index)">Eliminar</button></td>
                    </tr>
                </tbody>
            </table>
            <p v-else>No hay productos agregados</p>
        </div>

        <div class="total-section">
            <h3>Total: ${{ total }}</h3>
        </div>

        <div class="botones">
            <button @click="guardarVenta" class="btn-guardar">Guardar</button>
            <button @click="limpiarFormulario" class="btn-cancelar">Cancelar</button>
        </div>
    </div>
</template>

<script>
import ProductoClient from '@/clients/ProductoClient'; // Opcional si usas cliente, abajo uso fetch directo

export default {
    name: 'Venta',
    data() {
        return {
            numeroVenta: '',
            cedula: '', // En el template se llama cedula
            codigoBarras: '',
            cantidad: 1,
            productos: [], // Lista visual para el frontend
            mensaje: null
        }
    },
    computed: {
        // El cálculo visual se mantiene para el usuario, aunque el backend recalcula al guardar
        total() {
            return this.productos.reduce((sum, p) => sum + parseFloat(p.subtotal), 0).toFixed(2);
        }
    },
    methods: {
        async agregarProducto() {
            if (!this.codigoBarras || !this.cantidad) {
                this.mostrarMensaje('Ingrese código y cantidad', 'error');
                return;
            }

            try {
                // 1. Buscamos el producto en el Backend
                // Asegúrate que tu endpoint en Quarkus sea correcto (ej: /productos/{id})
                const response = await fetch(`http://localhost:8080/productos/${this.codigoBarras}`);

                if (!response.ok) {
                    throw new Error('Producto no encontrado');
                }

                const productoDb = await response.json();

                // 2. VALIDACIÓN DE STOCK (Requisito PDF)
                // Verificamos si la cantidad solicitada supera el stock real
                if (this.cantidad > productoDb.stock) {
                    this.mostrarMensaje(`Stock insuficiente. Disponible: ${productoDb.stock}`, 'error');
                    return;
                }

                // 3. Si pasa la validación, agregamos a la lista visual
                const productoVenta = {
                    codigoBarras: productoDb.codigoBarras,
                    descripcion: productoDb.nombre, // Asegúrate que el backend devuelva 'nombre'
                    cantidad: this.cantidad,
                    precioUnitario: productoDb.precio,
                    subtotal: (productoDb.precio * this.cantidad).toFixed(2)
                };

                this.productos.push(productoVenta);

                // Limpiar campos
                this.codigoBarras = '';
                this.cantidad = 1;
                this.mostrarMensaje('Producto agregado correctamente', 'exito');

            } catch (error) {
                console.error(error);
                this.mostrarMensaje('Error: Producto no encontrado o error de conexión', 'error');
            }
        },

        eliminarProducto(index) {
            this.productos.splice(index, 1);
        },

        async guardarVenta() {
            if (!this.numeroVenta || !this.cedula || this.productos.length === 0) {
                this.mostrarMensaje('Complete todos los campos y agregue productos', 'error');
                return;
            }

            // 4. PREPARAR EL JSON PARA EL BACKEND
            // El backend solo necesita ID de venta, cliente y lista de (codigo + cantidad)
            const ventaPayload = {
                numeroVenta: this.numeroVenta,
                cedulaCliente: this.cedula, // Mapeamos al nombre que espera la Entidad Java
                detalles: this.productos.map(p => ({
                    codigoBarras: p.codigoBarras,
                    cantidad: p.cantidad
                    // NO enviamos precio ni subtotal, el Backend lo calcula por seguridad
                }))
            };

            try {
                const response = await fetch('http://localhost:8080/ventas', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(ventaPayload)
                });

                if (response.ok) {
                    this.mostrarMensaje('Venta guardada exitosamente', 'exito');
                    this.limpiarFormulario();
                } else {
                    const errorText = await response.text();
                    this.mostrarMensaje('Error al guardar: ' + errorText, 'error');
                }
            } catch (error) {
                this.mostrarMensaje('Error de conexión con el servidor', 'error');
            }
        },

        limpiarFormulario() {
            this.numeroVenta = '';
            this.cedula = '';
            this.productos = [];
            this.mensaje = null;
        },

        mostrarMensaje(texto, tipo) {
            this.mensaje = { texto, tipo };
            setTimeout(() => this.mensaje = null, 4000);
        }
    }
}
</script>

<style>
.venta-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
    background-color: #f5f5f5;
}

h1 {
    color: #333;
    text-align: center;
    margin-bottom: 30px;
}

h2 {
    color: #555;
    margin-top: 25px;
    border-bottom: 2px solid #007bff;
    padding-bottom: 10px;
}

h3 {
    color: #333;
    text-align: right;
    font-size: 20px;
}

.form-section,
.productos-section,
.lista-productos,
.total-section {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-group {
    margin-bottom: 15px;
}

label {
    display: block;
    margin-bottom: 5px;
    color: #555;
    font-weight: bold;
}

input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
}

input:focus {
    outline: none;
    border-color: #007bff;
    background-color: #f0f8ff;
}

.agregar-producto {
    display: grid;
    grid-template-columns: 1fr 1fr auto;
    gap: 15px;
    align-items: flex-end;
}

button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    transition: 0.3s;
}

button {
    background-color: #28a745;
    color: white;
}

button:hover {
    background-color: #218838;
}

.btn-guardar {
    background-color: #007bff;
}

.btn-guardar:hover {
    background-color: #0056b3;
}

.btn-cancelar {
    background-color: #dc3545;
}

.btn-cancelar:hover {
    background-color: #c82333;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
}

th {
    background-color: #007bff;
    color: white;
    padding: 12px;
    text-align: left;
}

td {
    padding: 12px;
    border-bottom: 1px solid #ddd;
}

tr:hover {
    background-color: #f9f9f9;
}

.mensaje {
    padding: 12px;
    margin-top: 15px;
    border-radius: 4px;
    text-align: center;
    font-weight: bold;
}

.mensaje.exito {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.mensaje.error {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.botones {
    display: flex;
    gap: 10px;
    justify-content: center;
}

p {
    text-align: center;
    color: #999;
}

@media (max-width: 600px) {
    .agregar-producto {
        grid-template-columns: 1fr;
    }
}
</style>
