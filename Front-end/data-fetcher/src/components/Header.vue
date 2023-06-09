<script>
    export default {
        props: {
            text:String
        }
    }
</script>

<template>
    <!-- Reusable component to create a header -->
    <header class="text-blue-600 text-bold text-3xl mb-5">{{this.text}}</header>
</template>