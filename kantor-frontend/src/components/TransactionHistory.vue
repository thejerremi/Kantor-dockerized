<template>
  <v-card height="49vh" variant="tonal">
    <v-card-title>Historia transakcji</v-card-title>
    <v-card-text>
      <v-data-table
        v-model:sort-by="sortBy"
        :headers="headers"
        :items="userTransactions"
        :items-per-page="5"
        :items-per-page-options="options"
        no-data-text="Wykonaj swoją pierwszą transakcję!"
        items-per-page-text="Transakcji na stronę"
      >
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { useStore } from "vuex";
import moment from "moment";
import { onMounted, computed, watch } from "vue";
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
onMounted(async () => {
  await store.dispatch("fetchUserTransactions");
});
watch(userTransactions, (newValue) => {
  newValue.forEach((transaction) => {
    transaction.type = transactionTypes[transaction.type];
    transaction.amount = `${transaction.amount} PLN`;
    transaction.createdAt = moment(transaction.createdAt).format("DD-MM-YYYY HH:mm");
  });
});
</script>

<style scoped></style>