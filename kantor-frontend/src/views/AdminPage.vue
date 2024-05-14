<template>
  <v-main class="admin-view">
    <v-card class="ma-12" height="85vh" variant="outlined">
      <v-card-title class="text-white">Panel administratora</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-card class="ma-2" height="200" outlined>
              <v-card-title>Zarządzaj użytkownikiem</v-card-title>
              <v-card-text>
                <v-text-field v-model="userEmail" label="Email użytkownika" outlined></v-text-field>
              </v-card-text>
              <v-card-actions>
                <v-btn @click="changeRole('USER')" :disabled="!userEmail">Zmień rolę na USER</v-btn>
                <v-btn @click="changeRole('ADMIN')" :disabled="!userEmail">Zmień rolę na ADMIN</v-btn>
                <v-btn @click="fetchUserTransactions()" :disabled="!userEmail">Wczytaj historię transakcji</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
          <v-col cols="12" md="6">
            <v-card class="ma-2" height="200" outlined>
              <v-card-title>Kursy walut</v-card-title>
              <v-card-text>
                <v-row>
                  <v-col cols="4">
                    <v-select
                  v-model="selectedCurrency"
                  :items="currencyOptions"
                  label="Wybierz walutę"
                  outlined
                ></v-select>
              </v-col>
                <v-col cols="4">
                  <v-text-field v-model="askValue" label="Kurs kupna" outlined></v-text-field>
                </v-col>
                <v-col cols="4">
                  <v-text-field v-model="bidValue" label="Kurs sprzedaży" outlined></v-text-field>
                </v-col>
                </v-row>
              </v-card-text>
              <v-card-actions>
                <v-btn @click="saveCurrencyRate()" :disabled="!selectedCurrency">Zapisz kurs waluty {{ selectedCurrency }}</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card height="49vh" variant="tonal">
    <v-card-title>Historia transakcji</v-card-title>
    <v-card-text>
      <v-data-table
        v-model:sort-by="sortBy"
        :headers="headers"
        :items="userTransactions"
        :items-per-page="5"
        :items-per-page-options="options"
        items-per-page-text="Transakcji na stronę"
      >
      </v-data-table>
    </v-card-text>
  </v-card>
    </v-card>
    
  </v-main>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useStore } from "vuex";
import moment from "moment";
import AdminService from '@/services/AdminService';
onMounted(async () => {
  await store.dispatch("fetchUserTransactions");
  currencyOptions.value = currencyRates.value.map(rate => rate.code)

});
//Currency rates management
const currencyRates = computed (() => store.state.currencyRates);
const currencyOptions = ref([]);
const selectedCurrency = ref('');
const askValue = ref(0);
const bidValue = ref(0);
watch(selectedCurrency, () => {
  if (selectedCurrency.value) {
    const rate = currencyRates.value.find(rate => rate.code === selectedCurrency.value);
    askValue.value = rate.ask;
    bidValue.value = rate.bid;
  }
});
const saveCurrencyRate = async () => {
  await store.dispatch("saveCurrencyRates", { selectedCurrency: selectedCurrency.value, askValue: askValue.value, bidValue: bidValue.value })
};
//User management
const userEmail = ref('');
const changeRole = async (role) => {
  if(role === 'USER')
    await AdminService.changeRoleToUser(userEmail.value, role);
  else if(role === 'ADMIN'){
    console.log(userEmail.value);
    
    await AdminService.changeRoleToAdmin(userEmail.value, role).then(() => {
      console.log('Zmieniono na admina');
    }).catch((error) => {
      console.log(error);
    });
  }
};
//User transactions
const fetchUserTransactions = (async () => {
  console.log('Pobieram transakcje dla uzytkownika: ', userEmail.value);
  await store.dispatch("fetchUserTransactionsAdmin", userEmail.value);
});
const store = useStore();
const userTransactions = computed(() => store.state.userTransactions);
const options = [5];
const headers = [
  { title: "Typ transakcji", value: "type" },
  { title: "Kwota", value: "amount" },
  { title: "Data operacji", value: "createdAt" },
  { title: "Wymieniona waluta", value: "exchangedCurrency"}
];
const transactionTypes = {
  BUY: "Kupno waluty",
  SELL: "Sprzedaż waluty",
  DEPOSIT: "Depozyt",
  WITHDRAW: "Wypłata",
};
const sortBy = [{ key: "createdAt", order: "desc" }];

watch(userTransactions, (newValue) => {
  newValue.forEach((transaction) => {
    transaction.type = transactionTypes[transaction.type];
    transaction.amount = `${transaction.amount} PLN`;
    transaction.createdAt = moment(transaction.createdAt).format("DD-MM-YYYY HH:mm");
  });
});
</script>

<style>
.admin-view {
  height: 100vh;
  width: 100vw;
  background: linear-gradient(0deg, rgba(255,255,255,1) 0%, rgba(0,0,0,1) 100%);
}
</style>