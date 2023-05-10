import { ACCESS_TOKEN } from "../../constants";
import Navbar from "../components/Navbar";
import React from "react";
import Deposit from "./components/Deposit";



function DepositPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <Navbar/>
        <Deposit/>
      </div>
    );
  }
  
  export default DepositPage;