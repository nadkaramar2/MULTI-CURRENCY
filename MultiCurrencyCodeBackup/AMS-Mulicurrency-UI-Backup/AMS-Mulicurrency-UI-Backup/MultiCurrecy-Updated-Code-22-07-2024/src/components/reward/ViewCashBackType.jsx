// export default ViewChannel
import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewChannel() {
  const [alldata, setAlldata] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    viewChannel();
  }, [tcount, itemPage]);

  const viewChannel = async () => {
    try {
      const response = await amsApi.post(
        `cashback/viewTypeCreation/pagination/${itemPage}/${tcount}`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.cashbackTypeSearch);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };


  useEffect(() => {
    if (searchQuery === "" || searchQuery === " ") {
      viewChannel();
    }
  }, [searchQuery]);

  const Search = async () => {
    try {
      const response = await amsApi.post(
        `cashback/cashBackTypeSearch/pagination/20/1`,
        {
          keyword: searchQuery,
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.cashbackTypeSearch);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  
  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View CashBack Type
              </p>
            </div>
          </div>
        </div>
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={viewChannel}
        /> */}
        <div className="overflow-x-auto relative shadow-md sm:rounded-lg">
          <>
            <div className="table-wrp block max-h-[30rem]  max-w-[33rem]">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th scope="col" className="py-2.5 px-6  whitespace-nowrap ">
                    Created Date
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                    CashBack Type
                  </th>
                  <th scope="col" className="py-2.5 px-8  whitespace-nowrap ">
                    CashBack Description
                  </th>
                  <th scope="col" className="py-2.5 px-8  whitespace-nowrap ">
                    GL account Type
                  </th>
                  <th scope="col" className="py-2.5 px-8  whitespace-nowrap ">
                    Channel Code
                  </th>

                  <th scope="col" className="py-2.s px-8  whitespace-nowrap ">
                    Created By
                  </th>
                </thead>
                <tbody>
                  <>
                    {alldata.map(
                      ({
                        cashbackType,
                        cashbackDescr,
                        glAccountType,
                        glAccountNumber,
                        createdDate,
                        createdBy,
                      }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b hover:bg-blue-200
                    border-blue-400"
                        >
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {createdDate || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {cashbackType || "-"}
                            </div>
                          </td>
                          <td className="px-8 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {cashbackDescr || "-"}
                            </div>
                          </td>
                          <td className="px-8 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {glAccountType || "-"}
                            </div>
                          </td>
                          <td className="px-8 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {glAccountNumber || "-"}
                            </div>
                          </td>

                          <td className="px-8 py-2.5 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {createdBy || "-"}
                            </div>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}

// import React from 'react'

// const ViewCashBackType = () => {
//   return (
//     <div>ViewCashBackType</div>
//   )
// }

// export default ViewCashBackType
