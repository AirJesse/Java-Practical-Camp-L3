import axios from "axios";
import { da } from "element-plus/es/locale";
const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 5000
});

service.defaults.headers.post['Content-Type'] = 'application/json';

service.interceptors.request.use(config => {
    const token = localStorage.getItem('satoken');
    if (token) {
        config.headers['satoken'] = token;
    }
    return config;
},
    error => {
        console.error(error);
        return Promise.reject(error);
    }

);

service.interceptors.response.use(
    response => {
        return response;
    }, error => {
        // 处理响应错误
        console.error(error);
        return Promise.reject(error);
    }
)
export default (url, method, params) => {
    return service({
        url: url,
        method: method,
        params: params,
    }).then(res => {
        if (res.status >= 200 && res.status < 300) {
            return res;
        } else {
            return Promise.reject(res.data);
        }
    }).catch(err => {
        return Promise.reject(err);
    })
}

export function get(url, params) {
    return service({
        url: url,
        params: params,
        method: 'get'
    }).then(res => {
        if (res.status >= 200 && res.status < 300) {
            return res;
        } else {
            return Promise.reject(res.data);
        }
    }).catch(err => {
        return Promise.reject(err);
    })
}

export function post(url, data) {
    return service({
        url: url,
        method: 'post',
        data: data
    }).then(res => {
        if (res.status >= 200 && res.status < 300) {
            return res;
        } else {
            return Promise.reject(res.data);
        }
    }).catch(err => {
        return Promise.reject(err);
    })
}

