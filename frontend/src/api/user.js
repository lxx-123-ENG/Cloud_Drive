import request from '@/utils/request';

//const baseURL = '/user/user';

// 用户登录接口
export const login = (params) => request({
    url: '/user/user/login',
    method: 'POST',
    data: params//自动转为 JSON
})

export const userRegister = (params) => {
    return request({
        url: '/user/user/register', // 后端注册接口地址（与你配置的excludePathPatterns一致）
        method: 'POST',
        data: params // 传递注册参数
    })
}
export const loginByEmail = (params) => {
    return request({
        url: '/user/user/loginByEmail',
        method: 'POST',
        data: params //{ email: "xxx@xxx.com", code: "123456" }
    })
}