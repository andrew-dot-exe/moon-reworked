<template>
  <div class="master-screen-acount">
    <div class="logo-container">
      <svg xmlns="http://www.w3.org/2000/svg" width="388" height="56" viewBox="0 0 388 56" fill="none">
        <path d="M219 0L245.685 31.6863H245.841V0H262.226V56H245.841L219.157 24.3137H219V56H202.616V0H219Z"
          fill="white" />
        <path
          d="M0.5 0H17.4172L35.8155 35.6863H35.9715L54.3698 0H71.287V56H54.9156V30.6667H54.7596L40.8829 56H30.9041L17.0274 30.6667H16.8714V56H0.5V0Z"
          fill="white" />
        <path d="M284.774 56H280.854V52.15L304.876 0H308.796V3.85L284.774 56Z" fill="#BCFE37" />
        <path
          d="M352.368 5.2606L335.401 28L354.901 51.1636V56H350.089L329.661 31.4788H320.375V56H313.453V0H320.375V24.6909H329.661L347.135 0H352.368V5.2606Z"
          fill="#BCFE37" />
        <path d="M363.478 56H359.558V52.15L383.58 0H387.5V3.85L363.478 56Z" fill="#BCFE37" />
        <path fill-rule="evenodd" clip-rule="evenodd"
          d="M73.6155 27.5677C73.6155 45.0242 85.3478 55.6664 105.255 55.6664L168.572 56C188.479 56 200.287 45.6914 200.287 28.2349C200.287 10.7032 188.516 0.270827 168.61 0.0610741C155.569 -0.0763416 136.932 0.0610861 136.932 0.0610861H105.255C85.3478 0.0605458 73.6155 10.036 73.6155 27.5677ZM105.283 42.9333C95.8975 42.9333 90.3481 37.0484 90.3481 27.5677C90.3481 18.0118 95.8975 13.0667 105.283 13.0667H146.265L107.146 42.9262L105.283 42.9333ZM183.635 28.2349C183.635 18.679 178.081 13.0667 168.619 13.0667H166.756L127.637 42.9333H168.619C178.081 42.9333 183.635 37.7156 183.635 28.2349Z"
          fill="white" />
      </svg>
    </div>

    <form class="main-container" @submit.prevent="handleLogin">
      <div class="create-account">
        <div class="titile-block">Вход</div>

        <div class="account-information">
          <div class="div">
            <div class="title">
              <div class="text-wrapper">Почта</div>
            </div>

            <div class="value">
              <input type="email" id="email" v-model="email" required class="text-wrapper-2" />
            </div>
          </div>

          <div class="div">
            <div class="title">
              <div class="text-wrapper">Пароль</div>
            </div>

            <div class="value">
              <input type="password" id="password" v-model="password" required class="text-wrapper-2">
            </div>
          </div>
        </div>
      </div>

      <button class="button-sign-in">
        <div class="text-wrapper-3">Войти</div>
      </button>
    </form>
  </div>
</template>



<script setup lang="ts">
import { ref } from 'vue';
import userApi from '@/components/user/userApi';
import { useComponentStore } from '../stores/componentStore';

const email = ref('');
const password = ref('');
const componentStore = useComponentStore();

const handleLogin = async () => {
  if (email.value && password.value) {
    const response: Promise<number> = userApi.login(email.value, password.value);
    if (await response == 200) {
      componentStore.setComponent("zoneChooser");
    }
  } else {
    alert("Please fill in all fields.");
    // replace with fancy error message
  }
};
</script>

<style scoped>
.master-screen-acount {
  align-items: center;
  background-color: #454545;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.master-screen-acount .logo-container {
  background-color: #454545;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  border-color: #a3a3a3;
  display: flex;
  padding: 24px 40px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  align-self: stretch;
}

.master-screen-acount .logo-moon {
  height: 56px !important;
  position: relative !important;
  width: 387px !important;
}

.master-screen-acount .main-container {
  display: flex;
  height: 464px;
  padding: 20px 20px 48px 20px;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
}

.master-screen-acount .create-account {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 48px;
  align-self: stretch;
}

.master-screen-acount .titile-block {
  color: #ffffff;
  font-family: "Feature Mono-Medium", Helvetica;
  font-size: 32px;
  font-weight: 500;
  letter-spacing: 1.60px;
  line-height: normal;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}

.master-screen-acount .account-information {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 24px;
  align-self: stretch;
}

.master-screen-acount .div {
  display: flex;
  height: 44px;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
}

.master-screen-acount .title {
  background-color: rgba(0, 0, 0, 0.80);
  display: flex;
  padding: 10px;
  align-items: center;
  gap: 10px;
  flex: 1 0 0;
}

.master-screen-acount .text-wrapper {
  color: #FFF;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
}

.master-screen-acount .value {
  align-items: center;
  border: 1px solid rgba(0, 0, 0, 0.80);
  display: flex;
  flex: 1;
  flex-grow: 1;
  gap: 10px;
  justify-content: flex-end;
  padding: 10px;
  position: relative;
}

.master-screen-acount .text-wrapper-2 {
  display: flex;
  padding: 11px;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  flex: 1 0 0;
}

.master-screen-acount .title-2 {
  color: #ffffff;
  font-family: "Feature Mono-Regular", Helvetica;
  font-size: 24px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: normal;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}

.master-screen-acount .div-wrapper {
  align-items: center;
  border: 1px solid;
  border-color: #000000cc;
  display: flex;
  flex: 1;
  flex-grow: 1;
  gap: 10px;
  height: 44px;
  justify-content: flex-end;
  padding: 10px;
  position: relative;
}

.master-screen-acount .button-sign-in {
  align-items: center;
  background-color: #bbfe37;
  display: inline-flex;
  flex: 0 0 auto;
  gap: 10px;
  justify-content: center;
  padding: 16px 24px;
  position: relative;
}

.master-screen-acount .text-wrapper-3 {
  color: #000000;
  font-family: "Feature Mono-Medium", Helvetica;
  font-size: 24px;
  font-weight: 500;
  letter-spacing: 1.20px;
  line-height: normal;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}
</style>
