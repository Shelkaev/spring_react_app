import { ACCESS_TOKEN } from "../constants";
import Main from "./components/Main";
import Navbar from "./components/Navbar";
import DebitCardsTable from "./debit_card/components/DebitCardsTable";
import React from "react";
import CreditCardsTable from "./credit_card/components/CreditCardsTable";
import DepositTable from "./deposit/components/DepositTable";


function MainPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
            <Navbar/>
            <Main/>
            <DebitCardsTable/>
            <CreditCardsTable/>
            <DepositTable/>
      </div>
    );
  }
  
  export default MainPage;