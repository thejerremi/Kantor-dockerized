import http from "../http-common";

class transactionService {
    deposit(data) {
        return http.post(`/transaction/deposit`,
            {
                amount: data
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    withdraw(data) {
        return http.post(`/transaction/withdraw`,
            {
                amount: data
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    buyCurrency(data) {
        return http.post(`/transaction/buy`,
            {
                exchangedCurrency: data.currency,
                amount: data.amount
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    sellCurrency(data) {
        return http.post(`/transaction/sell`,
            {
                exchangedCurrency: data.currency,
                amount: data.amount
            },
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    fetchUserTransactions() {
        return http.get(`/transaction`,
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
    update() {
        const url = `/test/`;
        return http.put(url);
    }
    delete() {
        return http.delete(`test`);
    }
}

export default new transactionService();
