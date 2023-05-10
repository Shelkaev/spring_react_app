import { ACCESS_TOKEN } from "../../constants";

import BlockedTransfers from "./components/BlockedTransfers";
import NavbarAdmin from "./components/NavbarAdmin";
import React from "react";


function TransferAdminPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <NavbarAdmin/>
        <BlockedTransfers/>
      </div>
    );
  }
  
  export default TransferAdminPage;