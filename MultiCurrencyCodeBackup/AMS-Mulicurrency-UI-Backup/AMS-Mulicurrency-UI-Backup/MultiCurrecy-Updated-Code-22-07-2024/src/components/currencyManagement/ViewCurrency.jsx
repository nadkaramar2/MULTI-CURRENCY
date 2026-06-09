import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import amsApi from "../../api/amsApi";
import { Link } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewCurrency() {
  const [alldata, setAlldata] = useState([]);
  const [totalElement, setTotalElement] = useState("");
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
    viewcurrencytype();
  }, [tcount, itemPage]);
  const viewcurrencytype = async () => {
    try {
      const response = await amsApi.post(`currency-master/viewCurrencyMaster`, {
        pageSize: itemPage,
        pageNumber: tcount,
      });

      if (response.data.code === "S0000") {
        setTotalElement(response.data.totalElementsSize);
        setAlldata(response.data.viewCurrencyTypeMaster);

        
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       viewcurrencytype();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `currency-master/getCurrencySearchList/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.currencySearchList);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };
  
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className=" sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                View Currency
              </p>
              
            </div>
          </div>
        </div>

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={totalElement}
          reCallApi={viewcurrencytype}
        /> */}
        <div className="overflow-x-auto relative shadow-md ">
          {alldata.length > 0 && (
            <>
              <AutoPagintationReport
                addPageperData={setItemPage}
                addCurrentPage={setTcount}
                searchData={setSearchQuery}
                pagePerData={itemPage}
                currentPage={tcount}
                tcount={alldata[0]?.strTotalCount}
                reCallApi={Search}
              />
              <div className="table-wrp block max-h-[33rem] max-w-[33rem] ">
                <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                  <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                    <th scope="col" className="py-2 px-6 whitespace-nowrap">
                      Country
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      Currency Code
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      Currency Symbol
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      Currency Description
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      Base Country
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      Status
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      GL Account Type
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      GL Account Number
                    </th>
                    <th scope="col" className="py-2 px-6  whitespace-nowrap ">
                      EDIT
                    </th>
                  </thead>

                  <tbody>
                    <>
                      {alldata.map(
                        ({
                          country,
                          currencyCode,
                          currencySymbol,
                          currencyDescr,
                          baseCountry,
                          status,
                          glAccountType,
                          glAccountNumber,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  country || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  currencyCode || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  currencySymbol || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  currencyDescr || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  baseCountry || "-"}
                              </div>
                            </td>

                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {status || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  glAccountType || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  glAccountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2 whitespace-nowrap">
                              <Link
                                to={`/editcurrency/${country}`}
                                title="Click for Update "
                              >
                                <svg
                                  xmlns="http://www.w3.org/2000/svg"
                                  fill="none"
                                  viewBox="0 0 24 24"
                                  strokeWidth={1.5}
                                  stroke="blue"
                                  className="w-6 h-6"
                                >
                                  <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                                  />
                                </svg>
                              </Link>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  </tbody>
                </table>
              </div>
            </>
          )}
        </div>
      </div>
      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
