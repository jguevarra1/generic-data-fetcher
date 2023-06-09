import API from './API'

export default {
    sendForm(data) {
        return API().post('/db', data)
    },

    getData() {
        return API().get('/db');
    },

    getConfig(id) {
        return API().get('/db/' + id)
    },

    getRequirements(fileType) {
        return API().get(fileType)
    },

    //function 
    converter(string) {
        string = string.toLowerCase().split('_');
        for (var i = 0; i < string.length; i++) {
            string[i] = string[i].charAt(0).toUpperCase() + string[i].substring(1);     
        }
        return string.join(' ');
    }
}