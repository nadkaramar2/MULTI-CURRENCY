import React, { useState } from "react";

const AppLayout = ({ children }) => {
  const dtata = localStorage.getItem("OpenModel");
  return (
    <>
      <main className="w-full">
        <div className="px-4 mx-auto w-full  ">{children}</div>
      </main>
    </>
  );
};

export default AppLayout;
