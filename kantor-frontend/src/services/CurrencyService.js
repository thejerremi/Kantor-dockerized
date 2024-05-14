import http from "../http-common";

class currencyService {
  getCurrencyRates() {
    const url = `/rates`;
    return http.get(url);
  }
  create(data) {
    return http.post(`/test`, data);
  }
  update() {
    const url = `/test/`;
    return http.put(url);
  }
  delete(){
    return http.delete(`test`);
  }
}

export default new currencyService();
