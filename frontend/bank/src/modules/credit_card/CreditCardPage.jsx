import { ACCESS_TOKEN } from "../../constants";

import Navbar from "../components/Navbar";
import React from "react";
import CreditCard from "./components/CreditCard";



function CreditCardPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <Navbar/>
        <CreditCard/>
      </div>
    );
  }
  
  export default CreditCardPage;