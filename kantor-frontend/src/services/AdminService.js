import http from "../http-common";

class adminService {
    fetchUserTransactions(data){
        return http.get(`/admin/transactions?email=${data}`,
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    changeRoleToUser(data) {
        return http.put(`/admin/roleuser`,
            {
                email: data
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    changeRoleToAdmin(data) {
        return http.put(`/admin/roleadmin`,
            {
                email: data
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    saveCurrencyRates(data){
        return http.put(`/admin/currency`,
            {
                code: data.selectedCurrency,
                ask: data.askValue,
                bid: data.bidValue
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
        }
}

export default new adminService();
