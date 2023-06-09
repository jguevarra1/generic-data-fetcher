<script>
  import Header from "../components/Header.vue"
  export default {
    props: {
        configList: Array,
    },
    components: {
      Header
    },
  }
</script>

<template>
  <div class=" flex flex-col flex-autow-[100%] h-[50vh] text-xl bg-white rounded-xl shadow-md shadow-blue shadow-blue-500/50 ">
    <div class="my-12 mx-12">
      <Header text="Recent Jobs" class="my-1"></Header>

      <!-- Only display the most recent 5 configs -->
      <div class="mx-5 list-decimal text-start flex flex-col content-start overflow-hidden ">
        <ol v-for="item in this.configList.slice(Math.max(this.configList.length - 5, 0))" >
          <router-link :to="'/status/' + item.status + '/' + item.id">
            <li class="py-1">
               <button class="text-center hover:underline">
                 {{item.configName}}
                </button>
             </li>
          </router-link>
        </ol>
      </div>

      <!-- Make this link an a tag because vue-router doesn't work with flowbite pop up form -->
      <!-- Note: this is just a workaround, and is hardcoded to localhost:3000, should be careful when changing the port -->
      <a href="http://localhost:3000/newConfig" class="text-xl hover:scale-105 text-white shadow-md bg-blue-600 rounded-md px-3 py-1 mx-5 mt-12">
        Add Job
        <svg xmlns="http://www.w3.org/2000/svg" class="inline text-right h-7 w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
        </svg>
      </a>
    </div>

    <!-- Show all config button -->
    <div class="flex items-end justify-center mt-3">
      <router-link :to="'/all'">
        <button class="underline underline-offset-3 text-blue-500">Show All</button>
      </router-link>
    </div>
  </div>
</template>