import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import { v4 as uuidv4 } from "uuid";
import AutoPagintation from "../../UI/AutoPagintation";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function CardDetails() {
  const [alldata, setAlldata] = useState([]);
  console.log("data"+alldata);
  let custid = localStorage.getItem("custid");
  let FirstName = localStorage.getItem("Firstname");
  let LastName = localStorage.getItem("LastName");
  const [searchQuery1, setSearchQuery1] = useState("");
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
    CardDetails();
  }, [tcount, itemPage]);

  const CardDetails = async () => {
    try {
      const response = await amsApi.post(
        `card-account-linkage/getLinkagCrdDetailsBsdOnCustId/pagination/${itemPage}/${tcount}`,
        // card-account-linkage/getLinkagCrdDetailsBsdOnCustIdSearch/pagination/1/10
        {
          strCustId: custid,
        }
      );
      if (response.status===200) {
        setAlldata(response.data.cardAccountLinkagelist);
        console.log(response.data.cardAccountLinkagelist);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       CardDetails();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `card-account-linkage/getLinkagCrdDetailsBsdOnCustIdSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.cardLinkedCustIdList);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        {/* <div className="min-h-full first-line:flex items-center justify-center "> */}
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-8 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between ">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Customer Basic Information
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-2 sm:p-2 bg-white">
                <div className="grid gap-4 mb-2 md:grid-cols-3 px-8 ">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer Id
                    </label>
                    <input
                      type="text"
                      id="GL Account Type"
                      defaultValue={custid}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer id"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Description"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer Name
                    </label>
                    <input
                      type="text"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer name"
                      defaultValue={
                        FirstName && LastName ? FirstName + " " + LastName : ""
                      }
                    />
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        <div className="bg-white overflow-x-auto relative shadow-md  px-8 ">
          <div className="overflow-x-auto relative shadow-md  ">
            <h2 className="font-normal md:font-bold">Card Details</h2>
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
          reCallApi={CardDetails}
        /> */}

        <div className="overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem]  ">
            <table className="w-full text-xs text-left text-black dark:text-blue-100 ">
              <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2 px-6">
                    CARD NUMBER
                  </th>
                  <th scope="col" className="py-2 px-6">
                    CARD TYPE
                  </th>
                  <th scope="col" className="py-2 px-6">
                    CARD DESCRIPTION
                  </th>
                  <th scope="col" className="py-2 px-6">
                    CARD STATUS
                  </th>
                </tr>
              </thead>
              <tbody>
                {alldata.length > 0 ? (
                  <>
                    {alldata.map(
                      ({
                        strCardNumber,
                        strCardType,
                        strCardDescription,
                        strCardStatus,
                      }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {strCardNumber || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {strCardType || ""}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {strCardDescription || ""}
                            </span>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {strCardStatus || "-"}
                            </span>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-1 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
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
