import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import AutoPagintation from "../../UI/AutoPagintation";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "../reports/AutoPagintationReport";

export default function ViewTransactionType() {
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
    viewTransaction();
  }, []);

  const viewTransaction = async () => {
    try {
      const response = await amsApi.post(
        `transaction_type_master/getTransactionTypeModelData`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.txnReportlist);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       viewTransaction();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `transaction_type_master/getTransactionTypeSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.txnTypeModelSearchlist);
       
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };

  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };
    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Transaction Type
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
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={viewTransaction}
        /> */}
        <div className="overflow-x-auto relative shadow-md">
          <>
            <div className="table-wrp block max-h-[30rem]">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnTypeId")}
                  >
                    TXN TYPE ID {renderSortArrow("strTxnTypeId")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap "
                    onClick={() => handleSort("strTxnTypeDescrp")}
                  >
                    TXN TYPE DESCRIPTION {renderSortArrow("strTxnTypeDescrp")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strTxnTypeKeyWord")}
                  >
                    TXN TYPE KEYWORD {renderSortArrow("strTxnTypeKeyWord")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strProcessingCode")}
                  >
                    PROCESSING CODE {renderSortArrow("strProcessingCode")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strGLAccountType")}
                  >
                    GL ACCOUNT TYPE {renderSortArrow("strGLAccountType")}
                  </th>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          strTxnTypeId,
                          strTxnTypeDescrp,
                          strTxnTypeKeyWord,
                          strProcessingCode,
                          strGLAccountType,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  strTxnTypeId || "N/A"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  strTxnTypeDescrp || "N/A"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  strTxnTypeKeyWord || "N/A"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {
                                  strProcessingCode || "N/A"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900 ">
                                {
                                  strGLAccountType || "N/A"}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className="px-6 py-2.5 text-center">
                        <span className="text-lg font-medium text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
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
    </AppLayout>
  );
}
