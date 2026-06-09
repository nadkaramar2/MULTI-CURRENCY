import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function FeesCollected() {
  
  const [fetchData, setFetchData] = useState([]);
  const storedToken = localStorage.getItem("token");
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

  useEffect(() => {
    FeesCollectedDeatils();
  }, [tcount, itemPage]);

  const FeesCollectedDeatils = async () => {
    try {
      const response = await amsApi.post(
        // `/feeTypeMaster/getFeesCollected`,
        `feeTypeMaster/getFeesCollected/Pagination/${itemPage}/${tcount}`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `${storedToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        setFetchData(response.data.feeCollectedList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

     useEffect(() => {
      if (searchQuery === "" || searchQuery === " ") {
          FeesCollectedDeatils();
      }
    }, [searchQuery]);

  const Search = async () => {
      try {
      const response = await amsApi.post(
        `feeTypeMaster/getFeesCollectedSearch/pagination/20/1`,
        {
          keyword: searchQuery,
        }
      );
      if (response.data.code === "S0000") {
        setFetchData(response.data.feeCollectedList);
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
    } else 
    {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };
  // const renderSortArrow = (column) => {
  //   if (column === sortColumn) {
  //     return sortOrder === "asc" ? "↑" : "↓";
  //   }
  //   return "↕";
  // };
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
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2">
          <div className="px-4  sm:px-10  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
              FeeColected GL Balanced
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <div className="overflow-hidden shadow  ">
            <div className=" px-4  sm:p-2 bg-white  ">
              <div className="gap-1 md:grid-cols-2  justify-between flex ">
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
          reCallApi={FeesCollectedDeatils}
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
        <div className="overflow-x-auto relative shadow-md ">
          <>
            <div className="table-wrp block max-h-[27rem] max-w-[33rem]">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th
                    scope="col"
                    className="py-2.5 px-20 whitespace-nowrap"
                    onClick={() => handleSort("feeType")}
                  >
                    Fee Type {renderSortArrow("feeType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-20 whitespace-nowrap"
                    onClick={() => handleSort("feeDescription")}
                  >
                    Description {renderSortArrow("feeDescription")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-20 whitespace-nowrap"
                    onClick={() => handleSort("glAccountNo")}
                  >
                    GL Account No {renderSortArrow("glAccountNo")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-20 whitespace-nowrap"
                    onClick={() => handleSort("glAccountBalance")}
                  >
                    Account Balance {renderSortArrow("glAccountBalance")}
                  </th>
                </thead>
                <tbody>
                  {sortedData
                    .filter(
                      (data) =>
                        data.feeType
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.feeDescription
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.glAccountNo
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase()) ||
                        data.glAccountBalance
                          .toLowerCase()
                          .includes(searchQuery.toLowerCase())
                    )
                    .map((data) => (
                      <tr
                        // key={uuidv4()}
                        className=" border-b dark:border-neutral-500"
                      >
                        <td className="px-20 py-2.5 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.feeType || "-"}
                          </div>
                        </td>
                        <td className="px-20 py-2.5 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.feeDescription || "-"}
                          </div>
                        </td>
                        <td className="px-20 py-2.5 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.glAccountNo || "-"}
                          </div>
                        </td>
                        <td className="px-20 py-2.5 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.glAccountBalance || "-"}
                          </div>
                        </td>
                      </tr>
                    ))}
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
