import { createStore } from "vuex";
import CurrencyService from '@/services/CurrencyService';
import AuthService from '@/services/AuthService';
import TransactionService from "@/services/TransactionService";
import AdminService from "@/services/AdminService";
const store = createStore({
    state: {
        user: null,
        userTransactions: [],
        currencyRates: [],
        snackbarVisible: false,
        snackbarMessage: ''
    },
    mutations: {
        setUser(state, user) {
            state.user = user
        },
        setUserTransactions(state, userTransactions) {
            state.userTransactions = userTransactions
        },
        setCurrencyRates(state, currencyRates) {
            state.currencyRates = currencyRates
            state.currencyRates.pop();
        },
        setSnackbarVisible(state) {
            state.snackbarVisible = true
            setTimeout(() => {
                state.snackbarVisible = false
            }, 3000)
        },
        setSnackbarMessage(state, message) {
            state.snackbarMessage = message
        }
    },
    actions: {
        showSnackbar(context, message) {
            context.commit('setSnackbarMessage', message)
            context.commit('setSnackbarVisible')
        },
        //Auth
        async registerUser({ commit }, userFormatted) {
            try {
                const response = await AuthService.registerUser(userFormatted);
                localStorage.setItem('token', response.data.access_token);
                commit('setUser', response.data.user);
                store.dispatch('showSnackbar', 'Rejestracja pomyślna!')

            } catch (e) {
                console.error('Registration failed:', e.response);
                store.dispatch('showSnackbar', 'Rejestracja nieudana! Być może istnieje już konto z podanym adresem email. Spróbuj ponownie.')
                // Optionally, handle more gracefully, e.g., by committing a mutation to set an error state
                // commit('setError', e.response.data.message);
            }
        },
        async getUserByToken({ commit }) {
            try {
                const response = await AuthService.getUserByToken();
                commit('setUser', response.data);
                store.dispatch('showSnackbar', 'Zalogowano pomyślnie!')
            } catch (e) {
                console.log("User not logged in")
            }
        },
        async loginUser({ commit }, userFormatted) {
            try {
                const response = await AuthService.loginUser(userFormatted);
                localStorage.setItem('token', response.data.access_token);
                commit('setUser', response.data.user);
                store.dispatch('showSnackbar', 'Zalogowano pomyślnie!')

            } catch (e) {
                console.error('Login failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas logowania. Niepoprawne dane. Spróbuj ponownie.')
                // Optionally, handle more gracefully, e.g., by committing a mutation to set an error state
                // commit('setError', e.response.data.message);
            }
        },
        async logoutUser({ commit }) {
            try {
                await AuthService.logoutUser();
                localStorage.removeItem('token');
                commit('setUser', null);
                store.dispatch('showSnackbar', 'Wylogowano pomyślnie!')
            } catch (e) {
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas wylogowywania. Spróbuj ponownie.')
            }
        },
        //Transactions
        async fetchUserTransactions(context) {
            try {
                const response = await TransactionService.fetchUserTransactions();
                context.commit('setUserTransactions', response.data);
            } catch (e) {
                console.error('Fetch user transactions failed:', e.response);
            }
        },
        async deposit(context, amount){
            try {
                await TransactionService.deposit(amount);
                await this.dispatch('getUserByToken');
                await this.dispatch('fetchUserTransactions');
                store.dispatch('showSnackbar', 'Wpłata zakończona pomyślnie!')
            } catch (e) {
                console.error('Deposit failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas wpłaty. Spróbuj ponownie.')
            }
        },
        async withdraw(context, amount){
            try {
                await TransactionService.withdraw(amount);
                await this.dispatch('getUserByToken');
                await this.dispatch('fetchUserTransactions');
                store.dispatch('showSnackbar', 'Wypłata zakończona pomyślnie!')
            } catch (e) {
                console.error('Withdraw failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas wypłaty. Spróbuj ponownie.')
            }
        },
        async buyCurrency(context, transaction){
            try {
                await TransactionService.buyCurrency(transaction);
                await this.dispatch('getUserByToken');
                await this.dispatch('fetchUserTransactions');
                store.dispatch('showSnackbar', 'Kupno waluty zakończone pomyślnie!')
            } catch (e) {
                console.error('Buy currency failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas kupna waluty. Spróbuj ponownie.')
            }
        },
        async sellCurrency(context, transaction){
            try {
                await TransactionService.sellCurrency(transaction);
                await this.dispatch('getUserByToken');
                await this.dispatch('fetchUserTransactions');
                store.dispatch('showSnackbar', 'Sprzedaż waluty zakończona pomyślnie!')
            } catch (e) {
                console.error('Sell currency failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas sprzedaży waluty. Spróbuj ponownie.')
            }
        },
        //Currency rates
        async fetchCurrencyRates(context) {
            const response = await CurrencyService.getCurrencyRates()
            context.commit('setCurrencyRates', response.data)
        },
        //Admin
        async fetchUserTransactionsAdmin(context, data) {
            try {
                const response = await AdminService.fetchUserTransactions(data);
                context.commit('setUserTransactions', response.data);
                store.dispatch('showSnackbar', 'Pobrano transakcje użytkownika ' + data)
            } catch (e) {
                console.error('Fetch user transactions failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd. Sprawdź czy użytkownik istnieje i spróbuj ponownie.')
            }
        },
        async saveCurrencyRates(context, currencyRates) {
            try {
                await AdminService.saveCurrencyRates(currencyRates);
                await this.dispatch('fetchCurrencyRates');
                store.dispatch('showSnackbar', 'Zapisano kursy waluty ' + currencyRates.selectedCurrency + ' pomyślnie!')
            } catch (e) {
                console.error('Save currency rates failed:', e.response);
                store.dispatch('showSnackbar', 'Wystąpił błąd podczas zapisywania kursów waluty. Spróbuj ponownie.')
            }
        }
    },
})

export default store