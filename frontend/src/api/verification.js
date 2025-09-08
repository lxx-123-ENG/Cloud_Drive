import request from '@/utils/request';

//发送验证码接口
export const sendEmail = (email) => {
    return request({
        url: '/verification/sendEmail',
        method: 'POST',
        params: {email}
    })
}

//发送登录时候的验证码
export const sendLoginEmail = (email) => {
    return request({
        url: '/verification/sendLoginEmail',
        method: 'POST',
        params: { email }
    })
}