<template>
  <div class="login-container">
    <h1>Login</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">Login</button>
    </form>
  </div>
</template>



<script lang="ts">
import userApi from '@/components/user/userApi';
import { useComponentStore } from '../stores/componentStore';

export default {
  name: "LoginView",
  data() {
    return {
      email: "",
      password: "",
      componentStore: null
    };
  },
  created() {
    this.componentStore = useComponentStore();
  },
  methods: {
    async handleLogin() {

      if (this.email && this.password) {
        const response: Promise<number> = userApi.login(this.email, this.password);
        if (await response == 200) {
          this.componentStore.setComponent("zoneChooser")
        }

      } else {
        alert("Please fill in all fields.");
        // replace with fancy error message
      }
    },
  },
};
</script>

<style scoped></style>
