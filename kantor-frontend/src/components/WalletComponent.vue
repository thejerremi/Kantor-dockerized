<template>
  <v-dialog
          transition="dialog-bottom-transition"
          width="auto"
          @click:outside="toggleOffWallet()"
        >
          <template v-slot:activator="{ props: activatorProps }">
            <v-btn
              v-bind="activatorProps"
              text="Transition from Bottom"
              class="pr-6"
            >{{ user.balance }} PLN
            <v-tooltip activator="parent" location="bottom">Wpłać lub wypłać
            złotówki</v-tooltip>
          </v-btn>
          </template>

          <template v-slot:default="{ isActive }">
            <v-card class="wallet">
              <v-toolbar title="Wpłać/Wypłać złotówki"></v-toolbar>

              <v-card-text v-if="deposit === false && withdraw === false">
                <v-row>
                  <v-col cols="6">
                    <v-btn class="wallet-button" @click="toggleDeposit()">
                      Wpłać
                    </v-btn>
                  </v-col>

                    <v-col cols="6">
                    <v-btn class="wallet-button" @click="toggleWithdraw()">
                      Wypłać
                    </v-btn>
                  </v-col>
                </v-row>
              </v-card-text>
              <v-card-text v-if="deposit">
                <v-btn icon="mdi-arrow-left" size="x-large" @click="toggleOffWallet()"></v-btn>
                <div class="text-h3 text-center">
                  Wpłać pieniądze
                  </div>
                      <v-form v-model="depositValid">
                        <v-text-field
                        v-model="amount"
                        label="Kwota"
                        :rules="amountRule"
                        outlined
                        dense
                        clearable
                        placeholder="Wprowadź kwotę"
                      ></v-text-field>
                      </v-form>
              </v-card-text>
              <v-card-text v-if="withdraw">
                <v-btn icon="mdi-arrow-left" size="x-large" @click="toggleOffWallet()"></v-btn>
                <div class="text-h3 text-center">
                  Wypłać pieniądze
                  </div>
                  <v-form v-model="withdrawValid">
                        <v-text-field
                        v-model="amount"
                        label="Kwota"
                        :rules="withdrawAmountRule"
                        outlined
                        dense
                        clearable
                        placeholder="Wprowadź kwotę"
                      ></v-text-field>
                      <v-text-field
                        label="Numer konta"
                        hint="00 0000 0000 0000 0000 0000 0000"
                        :rules="withdrawRule"
                        outlined
                        dense
                        clearable
                        >
                      </v-text-field>
                    </v-form>
              </v-card-text>
              <v-card-actions class="justify-end">
                <v-btn
                v-if="deposit"
                class="text-green"
                  text="Wpłać"
                  @click="depositProceed(); isActive.value = false;"
                  :disabled="!depositValid"
                ></v-btn>
                <v-btn
                v-if="withdraw"
                class="text-green"
                  text="Wypłać"
                  @click="withdrawProceed(); isActive.value = false;"
                  :disabled="!withdrawValid"
                ></v-btn>
                <v-btn
                class="text-red"
                  text="Anuluj"
                  @click="toggleOffWallet(); isActive.value = false;"
                ></v-btn>
              </v-card-actions>
            </v-card>
          </template>
        </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
const store = useStore();
const user = computed(() => store.state.user);
const amount = ref(0);
const deposit = ref(false);
const depositValid = ref(false);
const withdraw = ref(false);
const withdrawValid = ref(false);
const toggleOffWallet = (() => {
  deposit.value = false;
  withdraw.value = false;
  amount.value = 0;
});
const toggleDeposit = (() => {
  deposit.value = true;
});
const toggleWithdraw = (() => {
  withdraw.value = true;
});
const depositProceed = (async () => {
   await store.dispatch('deposit', amount.value);
   toggleOffWallet();
})
const withdrawProceed = (async () => {
  await store.dispatch('withdraw', amount.value);
  toggleOffWallet();
})
const amountRule = [
    v => !!v || 'Pole nie może być puste',
    v => /^\d+(\.\d{1,2})?$/.test(v) || 'Nieprawidłowa kwota'
]
const withdrawAmountRule = [
    v => !!v || 'Pole nie może być puste',
    v => /^\d+(\.\d{1,2})?$/.test(v) || 'Nieprawidłowa kwota',
    v => v < user.value.balance || 'Nie masz wystarczających środków'
]
const withdrawRule = [
    v => !!v || 'Pole nie może być puste',
    v => v.replace(/\s/g, '').length === 26 || 'Numer konta musi mieć 26 znaków',
    v => /^\d+$/.test(v.replace(/\s/g, '')) || 'Numer konta musi składać się z samych cyfr'
  ]


</script>

<style scoped>
.wallet{
  width: 70vw;
  height: 70vh;
  margin: 0 auto;
  padding: 0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #f5f5f5;
}
.wallet-button{
  width: 100vw;
  min-height: 450px;
  margin: 0 auto;
  padding: 0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  color: #000;
  font-size: 1.5rem;
  font-weight: 600;
}
.v-form{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50%;
}
</style>