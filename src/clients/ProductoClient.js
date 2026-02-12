import axios from "axios";

const URL = "http://localhost:8081/productos/api/v1.0/productos";

// Métodos
const crear = async (body) => {
    return axios.post(`${URL}`, body).then(r => r.data);
}
const listarTodos = async () => {
    return axios.get(`${URL}`).then(r => r.data);
}

const actualizar = async (id, body) => {
    return axios.put(`${URL}/${id}`, body).then(r => r.data);
}

const eliminar = async (id) => {
    return axios.delete(`${URL}/${id}`).then(r => r.data);
}

const listarPorId = async (id) => {
    return axios.get(`${URL}/${id}`).then(r => r.data);
}

// Fachadas

export const crearFachada = async (body) => {
    return await crear(body);
}

export const listarTodosFachada = async () => {
    return await listarTodos();
}

export const actualizarFachada = async (id, body) => {
    return await actualizar(id, body);
}

export const eliminarFachada = async (id) => {
    return await eliminar(id);
}

export const listarPorIdFachada = async (id) => {
    return await listarPorId(id);
}