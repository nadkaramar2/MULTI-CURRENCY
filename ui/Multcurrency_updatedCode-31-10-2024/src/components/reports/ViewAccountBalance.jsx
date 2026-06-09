import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import AutoPagintation from "../../UI/AutoPagintation";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "./AutoPagintationReport";
export default function ViewAccountBalance() {
  const [fetchData, setFetchData] = useState([]);
  const storedToken = localStorage.getItem("token");
 
  const [searchKeyword, setSearchKeyword] = useState("");
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
  // DATE AND TIME
  var currentdate = new Date();
  var time =
    currentdate.getHours() +
    ":" +
    currentdate.getMinutes() +
    ":" +
    currentdate.getSeconds();

  var date =
    currentdate.getDate() +
    "/" +
    (currentdate.getMonth() + 1) +
    "/" +
    currentdate.getFullYear();
 const [searchQuery, setSearchQuery] = useState("");
 const [itemPage, setItemPage] = useState(20);
 const [tcount, setTcount] = useState(1);
  useEffect(() => {
    ViewAccountCollectedDeatils();
  }, [tcount, itemPage]);

  const ViewAccountCollectedDeatils = async () => {
    try {
      const response = await amsApi.post(
        `account/getAccoutBalanceList/pagination/${itemPage}/${tcount}`,

        {},
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${storedToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        setFetchData(response.data.accountCreationList);
        // handleShowSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

 

     useEffect(() => {
       if (searchQuery === "" || searchQuery === " ") {
         ViewAccountCollectedDeatils();
       }
     }, [searchQuery]);

     const Search = async () => {
       try {
         const response = await amsApi.post(
           `account/acctbalanceSearch/pagination/10/1`,
           {
             keyword: searchQuery,
           }
         );
         if (response.data.code === "S0000") {
           setFetchData(response.data.accountCreationList);
           // handleShowSuccess(response.data.message);
         } else {
           handleShowError(response.data.message);
         }
       } catch (error) {
         // handleShowError(error.response.data.message);
       }
     };
  // sorted tabled data
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
  const sortedData = [...fetchData].sort((a, b) => {
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
      <div className="">
        <div className="w-full shadow-md mt-2">
          <div className="px-4  sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Account Balance
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1 mb-1 ">
          <div className="overflow-hidden shadow  ">
            <div className=" px-4  sm:p-2 bg-white  ">
              <div className="gap-4 md:grid-cols-2  justify-between flex ">
                <div>
                  {" "}
                  <div className=" w-full px-1 py-1 text-base font-normal text-gray-700 ">
                    Balance as on {date} {time}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={fetchData[0]?.strTotalCount}
          reCallApi={ViewAccountCollectedDeatils}
        /> */}
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={fetchData[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* Table */}
        <div className="overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem] max-w-[28rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
              <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-1.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("strAccountType")}
                >
                  Account Type {renderSortArrow("strAccountType")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9 whitespace-nowrap"
                  onClick={() => handleSort("strAccountNumber")}
                >
                  Account Number {renderSortArrow("strAccountNumber")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9  whitespace-nowrap"
                  onClick={() => handleSort("strCustId")}
                >
                  Cust Id {renderSortArrow("strCustId")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9  whitespace-nowrap"
                  onClick={() => handleSort("strCid")}
                >
                  CId {renderSortArrow("strCid")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9  whitespace-nowrap"
                  onClick={() => handleSort("strBid")}
                >
                  BId {renderSortArrow("strBid")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9  whitespace-nowrap"
                  onClick={() => handleSort("strAccountHolderName")}
                >
                  Account Name {renderSortArrow("strAccountHolderName")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-9  whitespace-nowrap"
                  onClick={() => handleSort("strClosingBalance")}
                >
                  Account Balance {renderSortArrow("strClosingBalance")}
                </th>
              </thead>
              <tbody>
                {sortedData.map((data) => (
                  <tr
                    key={uuidv4()}
                    className=" border-b dark:border-neutral-500"
                  >
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strAccountType || "-"} */}
                        {
                          data.strAccountType || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strAccountNumber || "-"} */}
                        {
                          data.strAccountNumber || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strCustId || "-"} */}
                        {data.strCustId || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strCid || "-"} */}
                        {data.strCid || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.bid || "-"} */}
                        {data.strBid || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strAccountHolderName || "-"} */}
                        {
                          data.strAccountHolderName || "-"}
                      </div>
                    </td>
                    <td className="px-9 py-2.5  text-end whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">
                        {/* {data.strClosingBalance || "-"} */}
                        {
                          data.strClosingBalance || "-"}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
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
