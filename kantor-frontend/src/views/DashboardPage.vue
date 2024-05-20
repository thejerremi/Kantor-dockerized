<template>
  <v-main>
    <v-row>
      <v-col cols="4">
        <div class="pa-6">
          <v-card class="ratesTable" variant="tonal">
            <v-card-title class="text-center">Aktualne kursy</v-card-title>
            <v-table>
              <thead>
                <tr>
                  <th class="text-left">Waluta</th>
                  <th class="text-left">Kup</th>
                  <th class="text-left">Sprzedaj</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in currencyRates" :key="item.name">
                  <td>
                    <span :class="getCountryFlag(item.code)" /> {{ item.code }}
                  </td>
                  <td>{{ item.ask }}</td>
                  <td>{{ item.bid }}</td>
                </tr>
              </tbody>
            </v-table>
          </v-card>
        </div>
      </v-col>
      <v-col cols="8" class="pt-8">
        <v-row class="fill-height">
          <v-col cols="12" class="px-6 pt-4">
            <v-card height="35vh" variant="tonal">
              <v-card-text>
                <v-tabs fixed-tabs>
                  <v-tab text="Kup" @click="buying = true; selectedCurrency = null"></v-tab>
                  <v-tab text="Sprzedaj" @click="buying = false; selectedCurrency = null"></v-tab>
                </v-tabs>
                <v-form v-model="buyValid" v-if="buying">
                <v-chip-group class="codeChips" filter mandatory>
                  <div v-for="item in currencyRates" :key="item.name">
                    <v-chip @click="selectedCurrency = item"><span :class="getCountryFlag(item.code)"></span>
                      {{ item.code }}</v-chip>
                  </div>
                </v-chip-group>
                <div v-if="selectedCurrency" class="text-h6 text-center">Kurs: {{ selectedCurrency.ask }}</div>
                <v-row>
                  <v-col cols="8">
                    <v-text-field class="pl-6" label="Numer bankowy" :rules="buyRule" placeholder="00 0000 0000 0000 0000 0000 0000"
                      hint="Upewnij się, że to konto przyjmie daną walutę!" outlined>
                    </v-text-field>
                  </v-col>
                  <v-col cols="4">
                    <v-text-field v-model="amount" class="pr-6" label="Kwota" :rules="buyAmountRule" outlined>
                    </v-text-field>
                  </v-col>
                </v-row>
                <div v-if="selectedCurrency" class="text-h5 text-center">
                  Aby zakupić {{ amount }} {{ selectedCurrency.code }}, musisz zapłacić {{ cost }} PLN
                </div>
              </v-form>
              <v-form v-model="sellValid" v-else>
                <v-chip-group class="codeChips" filter mandatory>
                  <div v-for="item in currencyRates" :key="item.name">
                    <v-chip @click="selectedCurrency = item"><span :class="getCountryFlag(item.code)"></span>
                      {{ item.code }}</v-chip>
                  </div>
                </v-chip-group>
                <div v-if="!selectedCurrency" class="text-h3 text-center pt-12">Wybierz walutę</div>
                <div v-if="selectedCurrency" class="text-h6 text-center">Kurs: {{ selectedCurrency.bid }}</div>
                <v-row>
                  <v-col cols="4">
                    <v-text-field v-if="selectedCurrency" v-model="amount" class="px-6" label="Kwota" :rules="sellAmountRule" outlined>
                    </v-text-field>
                  </v-col>
                  <v-col cols="8">
                    <div v-if="selectedCurrency" class="text-h6 text-center pt-3">
                    Przelej {{ amount }} {{ selectedCurrency.code }} na konto {{ sellBankAccount }}
                    <br>i naciśnij przycisk "Sprzedaj"
                  </div>
                  </v-col>
                  </v-row>
                  <div v-if="selectedCurrency" class="text-h5 text-center pt-3">
                    Sprzedając {{ amount }} {{ selectedCurrency.code }}, otrzymasz {{ gain }} PLN
                  </div>

              </v-form>
              </v-card-text>
              <v-card-actions class="mx-auto">
                <v-btn v-if="buying" class="bg-green" block :disabled="!buyValid || !selectedCurrency || cost < 0.01" @click="buyProceed()">Kup</v-btn>
                <v-btn v-if="!buying" class="mt-n6 bg-green" block :disabled="!sellValid || !selectedCurrency" @click="sellProceed()">Sprzedaj</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
          <v-col cols="12" class="pa-6">
            <transaction-history />
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-main>
</template>

<script setup>
import TransactionHistory from "@/components/TransactionHistory.vue";
import { ref, computed, watch } from "vue";
import { useStore } from "vuex";
import "/node_modules/flag-icons/css/flag-icons.min.css";
const store = useStore();
const user = computed(() => store.state.user);
const currencyRates = computed(() => store.state.currencyRates);
const getCountryFlag = (code) => {
  return `fi fi-${code.toLowerCase().substring(0, 2)}`;
};
const buying = ref(true);
const selectedCurrency = ref(null);
const amount = ref(0);
const buyValid = ref(false);
const buyAmountRule = [
    v => !!v || 'Pole nie może być puste',
    v => /^\d+(\.\d{1,2})?$/.test(v) || 'Nieprawidłowa kwota',
    v => v < user.value.balance || 'Nie masz wystarczających środków'
]
const buyRule = [
    v => !!v || 'Pole nie może być puste',
    v => v.replace(/\s/g, '').length === 26 || 'Numer konta musi mieć 26 znaków',
    v => /^\d+$/.test(v.replace(/\s/g, '')) || 'Numer konta musi składać się z samych cyfr'
  ]
const buyProceed = (async () => {
  store.dispatch('buyCurrency', { amount: cost.value, currency: selectedCurrency.value.code });
})
const cost = computed(() => {
  if (selectedCurrency.value) {
    return (amount.value * selectedCurrency.value.ask).toFixed(2);
  }
  return 0;
});
const sellValid = ref(false);
const sellAmountRule = [
    v => !!v || 'Pole nie może być puste',
    v => /^\d+(\.\d{1,2})?$/.test(v) || 'Nieprawidłowa kwota',
]
const gain = computed(() => {
  if (selectedCurrency.value) {
    return (amount.value * selectedCurrency.value.bid).toFixed(2);
  }
  return 0;
});
const sellBankAccount = ref('')
const sellProceed = (async () => {
  store.dispatch('sellCurrency', { amount: gain.value, currency: selectedCurrency.value.code });
})
watch(selectedCurrency, () => {
  amount.value = 0;
  if(buying.value===false){
    let bankAccount = "";
  for (let i = 0; i < 26; i++) {
    const randomDigit = Math.floor(Math.random() * 10);
    bankAccount += randomDigit.toString();
  }
  sellBankAccount.value = bankAccount;
  }
});
</script>

<style scoped>
.v-input{
  width: 100%;

}
.ratesTable {
  min-height: calc(100vh - 115px);
}
.codeChips{
  max-width: fit-content;
  margin-inline: auto;
}
.v-main {
  overflow: hidden;
}
</style>